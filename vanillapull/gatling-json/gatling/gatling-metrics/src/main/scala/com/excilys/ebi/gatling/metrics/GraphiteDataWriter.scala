/**
 * Copyright 2011-2013 eBusiness Information, Groupe Excilys (www.ebusinessinformation.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excilys.ebi.gatling.metrics

import java.io.{ BufferedWriter, IOException, OutputStreamWriter, Writer }
import java.net.Socket
import java.util.{ Timer, TimerTask }

import scala.collection.mutable

import com.excilys.ebi.gatling.core.config.GatlingConfiguration.configuration
import com.excilys.ebi.gatling.core.result.message.{ GroupRecord, RequestRecord, RunRecord, ScenarioRecord, ShortScenarioDescription }
import com.excilys.ebi.gatling.core.result.message.RecordEvent.{ END, START }
import com.excilys.ebi.gatling.core.result.writer.DataWriter
import com.excilys.ebi.gatling.core.util.StringHelper.END_OF_LINE
import com.excilys.ebi.gatling.core.util.TimeHelper.nowSeconds
import com.excilys.ebi.gatling.metrics.types.{ Metrics, RequestMetrics, UserMetric }

case object SendToGraphite

class GraphiteDataWriter extends DataWriter {

	private val rootPathPrefix = configuration.data.graphite.rootPathPrefix.split('.').toList
	private var metricRootPath: List[String] = Nil
	private val groupStack: mutable.Map[Int, List[String]] = mutable.Map.empty
	private val allRequests = new RequestMetrics
	private val perRequest: mutable.Map[List[String], RequestMetrics] = mutable.Map.empty
	private var allUsers: UserMetric = _
	private val usersPerScenario: mutable.Map[String, UserMetric] = mutable.Map.empty
	private var timer: Timer = _
	private var writer: Writer = _
	private val sanitizeStringMemo = mutable.Map.empty[String, String]
	private val sanitizeStringListMemo = mutable.Map.empty[List[String], List[String]]
	private val percentiles1 = configuration.charting.indicators.percentile1
	private val percentiles1Name = "percentiles" + percentiles1
	private val percentiles2 = configuration.charting.indicators.percentile2
	private val percentiles2Name = "percentiles" + percentiles2

	private def newWriter: Writer = {
		val socket = new Socket(configuration.data.graphite.host, configuration.data.graphite.port)
		new BufferedWriter(new OutputStreamWriter(socket.getOutputStream))
	}

	def onInitializeDataWriter(runRecord: RunRecord, scenarios: Seq[ShortScenarioDescription]) {
		metricRootPath = rootPathPrefix ::: List(runRecord.simulationId)
		allUsers = new UserMetric(scenarios.map(_.nbUsers).sum)
		scenarios.foreach(scenario => usersPerScenario += scenario.name -> new UserMetric(scenario.nbUsers))
		writer = newWriter
		timer = new Timer(true)
		timer.scheduleAtFixedRate(new SendToGraphiteTask, 0, 1000)
	}

	def onScenarioRecord(scenarioRecord: ScenarioRecord) {
		usersPerScenario(scenarioRecord.scenarioName).update(scenarioRecord)
		allUsers.update(scenarioRecord)
		scenarioRecord.event match {
			case START => groupStack += scenarioRecord.userId -> Nil
			case END => groupStack.remove(scenarioRecord.userId)
		}
	}

	def onGroupRecord(groupRecord: GroupRecord) {
		val userId = groupRecord.userId
		val userStack = groupStack(userId)
		val newUserStack = groupRecord.event match {
			case START => groupRecord.groupName :: userStack
			case END if (!userStack.isEmpty) => userStack.tail
			case _ =>
				error("Trying to stop a user that hasn't started?!")
				Nil
		}
		groupStack += userId -> newUserStack
	}

	def onRequestRecord(requestRecord: RequestRecord) {
		if (!configuration.data.graphite.light) {
			val currentGroup = groupStack(requestRecord.userId)
			val path = requestRecord.requestName :: currentGroup
			val metric = perRequest.getOrElseUpdate(path.reverse, new RequestMetrics)
			metric.update(requestRecord)
		}
		allRequests.update(requestRecord)
	}

	def onFlushDataWriter {
		writer.close
	}

	override def receive = uninitialized

	override def initialized: Receive = super.initialized.orElse {
		case SendToGraphite => sendMetricsToGraphite(nowSeconds)
	}

	private def sendMetricsToGraphite(epoch: Long) {

		def sanitizeString(s: String) = sanitizeStringMemo.getOrElseUpdate(s, s.replace(' ', '_').replace('.', '-').replace('\\', '-'))

		def sanitizeStringList(list: List[String]) = sanitizeStringListMemo.getOrElseUpdate(list, list.map(sanitizeString))

		def sendToGraphite(metricPath: MetricPath, value: Long) {
			writer.write(metricPath.toString)
			writer.write(" ")
			writer.write(value.toString)
			writer.write(" ")
			writer.write(epoch.toString)
			writer.write(END_OF_LINE)
		}

		def sendUserMetrics(scenarioName: String, userMetric: UserMetric) {
			val rootPath = MetricPath(List("users", sanitizeString(scenarioName)))
			sendToGraphite(rootPath + "active", userMetric.active)
			sendToGraphite(rootPath + "waiting", userMetric.waiting)
			sendToGraphite(rootPath + "done", userMetric.done)
		}

		def sendMetrics(metricPath: MetricPath, metrics: Metrics) {
			sendToGraphite(metricPath + "count", metrics.count)

			if (metrics.count > 0L) {
				sendToGraphite(metricPath + "max", metrics.max)
				sendToGraphite(metricPath + "min", metrics.min)
				sendToGraphite(metricPath + percentiles1Name, metrics.getQuantile(percentiles1))
				sendToGraphite(metricPath + percentiles2Name, metrics.getQuantile(percentiles2))
			}
		}

		def sendRequestMetrics(path: List[String], requestMetrics: RequestMetrics) = {
			val rootPath = MetricPath(sanitizeStringList(path))

			val (okMetrics, koMetrics, allMetrics) = requestMetrics.metrics

			sendMetrics(rootPath + "ok", okMetrics)
			sendMetrics(rootPath + "ko", koMetrics)
			sendMetrics(rootPath + "all", allMetrics)

			requestMetrics.reset
		}

		try {
			if (writer == null) writer = newWriter

			sendUserMetrics("allUsers", allUsers)
			usersPerScenario.foreach {
				case (scenarioName, userMetric) => sendUserMetrics(scenarioName, userMetric)
			}
			sendRequestMetrics(List("allRequests"), allRequests)
			if (!configuration.data.graphite.light)
				perRequest.foreach {
					case (requestName, requestMetric) => sendRequestMetrics(requestName, requestMetric)
				}

			writer.flush

		} catch {
			case e: IOException => {
				error("Error writing to Graphite", e)
				if (writer != null) {
					try {
						writer.close
					} catch {
						case _: Exception => // shut up
					} finally {
						writer = null
					}
				}
			}
		}
	}

	private class SendToGraphiteTask extends TimerTask {
		def run {
			self ! SendToGraphite
		}
	}

	private object MetricPath {

		def apply(elements: List[String]) = new MetricPath(metricRootPath ::: elements)
	}

	private class MetricPath(path: List[String]) {

		def +(element: String) = new MetricPath(path ::: List(element))

		override def toString = path.mkString(".")
	}
}


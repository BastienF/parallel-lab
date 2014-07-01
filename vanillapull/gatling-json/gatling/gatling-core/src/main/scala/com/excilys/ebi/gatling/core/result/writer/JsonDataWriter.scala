/**
 * Copyright 2011-2013 eBusiness Information, Groupe Excilys (www.ebusinessinformation.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excilys.ebi.gatling.writer

import java.io.FileOutputStream

import com.excilys.ebi.gatling.core.config.GatlingConfiguration.configuration
import com.excilys.ebi.gatling.core.config.GatlingFiles.simulationLogDirectory
import com.excilys.ebi.gatling.core.result.message.{RequestRecord, RunRecord, ScenarioRecord, ShortScenarioDescription}
import com.excilys.ebi.gatling.core.result.message.GroupRecord
import com.excilys.ebi.gatling.core.result.message.RecordType.{ACTION, GROUP, RUN, SCENARIO}
import com.excilys.ebi.gatling.core.util.FileHelper.TABULATION_SEPARATOR
import com.excilys.ebi.gatling.core.util.IOHelper.use
import com.excilys.ebi.gatling.core.util.StringHelper.END_OF_LINE

import grizzled.slf4j.Logging
import com.excilys.ebi.gatling.core.result.writer.DataWriter

object JsonDataWriter {

  val emptyField = " "
  val sanitizerPattern = """[\n\r\t]""".r

  def sanitize(input: String): String = Option(sanitizerPattern.replaceAllIn(input, " ")).getOrElse("")

  def serialize(requestRecord: RequestRecord, constantLog: String) = {
    new StringBuilder().append(constantLog)
      .append("\"ResponseTime\": \"").append((requestRecord.responseReceivingStartDate - requestRecord.requestSendingEndDate).toString).append("\"\n")
      .append("},\n")
      .toString.getBytes(configuration.core.encoding)
  }

  def serialize(runRecord: RunRecord) =
    new StringBuilder().toString.getBytes(configuration.core.encoding)

  def serialize(scenarioRecord: ScenarioRecord) = {
    new StringBuilder().toString.getBytes(configuration.core.encoding)
  }

  def serialize(groupRecord: GroupRecord) =
    new StringBuilder().toString.getBytes(configuration.core.encoding)
}

/**
 * File implementation of the DataWriter
 *
 * It writes the data of the simulation if a tabulation separated values file
 */
class JsonDataWriter extends DataWriter with Logging {

  import com.excilys.ebi.gatling.writer.JsonDataWriter._

  private val bufferSize = configuration.data.file.bufferSize
  private var bufferPosition = 0
  private val buffer = new Array[Byte](bufferSize)
  private var os: FileOutputStream = _
  private val implementation = System.getProperty("implementation")
  private val iterations = System.getProperty("iterations")
  private val users = System.getProperty("users")
  private val delay = System.getProperty("noSynchronization_delay")
  private val duration = System.getProperty("duration")
  private val server = System.getProperty("server")
  private val aws = System.getProperty("aws")
  private val language = System.getProperty("language")
  private val nbThreads = System.getProperty("nbThreads")
  private val consolidateReport = System.getProperty("consolidateReport")
  var constantLog: String = ""

  private var responseTimeSum:Long = 0;
  private var numberOfResponses = 0;

  private def getConstantLog(): String = {
    val synchronizationMode: String = System.getProperty("synchronizationMode")
    val entry: StringBuilder = new StringBuilder("{\n");
    entry.append("\"Implementation\": \"").append(implementation).append("\",\n");
    if (synchronizationMode != null && (synchronizationMode == "true"))
      entry.append("\"Iterations\": \"").append(iterations).append("\",\n");
    else
      entry.append("\"Delay\": \"").append(delay).append("\",\n");
    entry.append("\"Users\": \"").append(users).append("\",\n");
    entry.append("\"Duration\": \"").append(duration).append("\",\n");
    entry.append("\"Serv\": \"").append(server).append("\",\n");
    entry.append("\"AWS\": \"").append(aws).append("\",\n");
    entry.append("\"Lang\": \"").append(language).append("\",\n");
    entry.append("\"NbT\": \"").append(nbThreads).append("\",\n");
    entry.toString
  }

  private def flush {
    if (consolidateReport != null && consolidateReport != "false") {
      write(new StringBuilder().append(constantLog)
        .append("\"ResponseTime\": \"").append((responseTimeSum / numberOfResponses).toString).append("\",\n")
        .append("\"NumberOfRequest\": \"").append((numberOfResponses).toString).append("\"\n")
        .append("},\n")
        .toString.getBytes(configuration.core.encoding))
    }
    os.write(buffer, 0, bufferPosition)
    bufferPosition = 0
  }

  private def write(bytes: Array[Byte]) {
    if (bytes.length + bufferPosition > bufferSize) {
      flush
    }

    if (bytes.length > bufferSize) {
      // can't write in buffer
      warn("Buffer size " + bufferSize + " is not sufficient for message of size " + bytes.length)
      os.write(bytes)

    } else {
      System.arraycopy(bytes, 0, buffer, bufferPosition, bytes.length)
      bufferPosition += bytes.length
    }
  }

  override def onInitializeDataWriter(runRecord: RunRecord, scenarios: Seq[ShortScenarioDescription]) {
    val simulationLog = System.getProperty("logPath") + ".json"
    os = new FileOutputStream(simulationLog.toString, true)
    constantLog = getConstantLog()
  }

  override def onScenarioRecord(scenarioRecord: ScenarioRecord) {
    write(serialize(scenarioRecord))
  }

  override def onGroupRecord(groupRecord: GroupRecord) {
    write(serialize(groupRecord))
  }

  override def onRequestRecord(requestRecord: RequestRecord) {
    if (requestRecord.requestStatus.toString == "OK") {
      if (consolidateReport == null || consolidateReport == "false") {
        write(serialize(requestRecord, constantLog))
      }
      else
      {
        responseTimeSum += requestRecord.responseReceivingStartDate - requestRecord.requestSendingEndDate;
        numberOfResponses += 1;
      }
    }

  }

  override def onFlushDataWriter {

    info("Received flush order")

    use(os) {
      _ => flush
    }
  }
}
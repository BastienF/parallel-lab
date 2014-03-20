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
package com.excilys.ebi.gatling.core.result.terminator

import java.util.concurrent.CountDownLatch

import com.excilys.ebi.gatling.core.action.{ AkkaDefaults, BaseActor, system }
import com.excilys.ebi.gatling.core.config.GatlingConfiguration.configuration
import com.excilys.ebi.gatling.core.result.message.Flush

import akka.actor.{ ActorRef, Props }
import akka.dispatch.Future
import akka.pattern.ask

object Terminator extends AkkaDefaults {

	private val terminator = system.actorOf(Props[Terminator])

	def askInit(latch: CountDownLatch, endUserCount: Int): Future[Any] = {
		terminator ? Initialize(latch, endUserCount)
	}

	def askDataWriterRegistration(dataWriter: ActorRef): Future[Any] = {
		terminator ? RegisterDataWriter(dataWriter)
	}

	def endUser {
		terminator ! EndUser
	}

	def forceTermination {
		terminator ! ForceTermination
	}
}

class Terminator extends BaseActor {

	import context._

	/**
	 * The countdown latch that will be decreased when all message are written and all scenarios ended
	 */
	private var latch: CountDownLatch = _
	private var endUserCount: Int = _

	private var registeredDataWriters: List[ActorRef] = Nil

	def uninitialized: Receive = {

		case Initialize(latch, userCount) =>
			info("Initializing")
			this.latch = latch
			this.endUserCount = userCount * configuration.data.dataWriterClasses.size
			registeredDataWriters = Nil
			context.become(initialized)
			sender ! true
			info("Initialized")
	}

	def flush {
		Future.sequence(registeredDataWriters.map(_ ? Flush))
			.onSuccess {
				case _ =>
					latch.countDown
					context.unbecome
			}.onFailure {
				case e: Exception => error(e)
			}
	}

	def initialized: Receive = {

		case RegisterDataWriter(dataWriter: ActorRef) =>
			registeredDataWriters = dataWriter :: registeredDataWriters
			sender ! true
			info("DataWriter registered")

		case EndUser =>
			endUserCount = endUserCount - 1
			if (endUserCount == 0) flush

		case ForceTermination => flush
	}

	def receive = uninitialized
}
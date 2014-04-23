package com.octo.vanillapull.service.scala

import _root_.scala.concurrent.duration.Duration
import _root_.scala.concurrent.{Await, Future}
import javax.annotation.{PostConstruct, PreDestroy}
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import akka.actor._
import com.octo.vanillapull.actor.{ResultListener, Work, Master}
import akka.util.Timeout
import org.slf4j.{LoggerFactory, Logger}
import akka.pattern.Patterns

@Profile(Array("akka"))
@Service
class AkkaMonteCarlo extends PricingService {
  final val logger: Logger = LoggerFactory.getLogger(classOf[AkkaMonteCarlo])

  private var system: ActorSystem = null
  private var master: ActorRef = null

  @PostConstruct override def init: Unit = {
    system = ActorSystem.create("MonteCarloSystem")
    master = system.actorOf(new Props(() => new Master(interestRate)))
    Unit
  }

  @PreDestroy override def cleanUp: Unit = {
    system.shutdown
  }

  def calculatePrice(maturity: Double, spot: Double, strike: Double, volatility: Double): Double = {
    // start the calculation
    val work: Work = new Work(numberOfIterations, maturity, spot, strike, volatility)

    // Creating agregator - use to aggregate result from master
    val agregator: ActorRef = system.actorOf(new Props(new UntypedActorFactory {
      def create: UntypedActor = {
        val agregator: ResultListener = new ResultListener
        agregator.maturity = maturity
        agregator.master = master
        agregator.interestRate = interestRate
        return agregator
      }
    }))

    val timeout: Timeout = new Timeout(Duration.create(60, "seconds"))

    val future: Future[AnyRef] = Patterns.ask(agregator, work, timeout)

    var bestPremiumsComputed: Double = 0

    try {
      if (logger.isDebugEnabled) logger.debug("[SERVICE] Waiting thread")
      bestPremiumsComputed = Await.result(future, timeout.duration).asInstanceOf[Double]
      if (logger.isDebugEnabled) logger.debug("[SERVICE] Releasing thread")
    }
    catch {
      case e: Exception => {
        throw new RuntimeException(e)
      }
    }
    finally {
    }

    return bestPremiumsComputed
  }
}
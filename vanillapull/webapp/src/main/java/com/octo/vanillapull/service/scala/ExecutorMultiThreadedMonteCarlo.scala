package com.octo.vanillapull.service.scala

import com.octo.vanillapull.util.StdRandom
import java.util.concurrent._
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile(Array("executor"))
@Service
class ExecutorMultiThreadedMonteCarlo extends PricingService {
  private val pool: ExecutorService = Executors.newFixedThreadPool(processors)

  private class MonteCarloTask(nbIterations: Long, maturity: Double, spot: Double, strike: Double, volatility: Double) extends Callable[Double] {
    private[service] var bestPremiumsComputed: Double = 0

    def call: Double = {
      {
        var i: Long = 0
        while (i < nbIterations) {
          {
            val gaussian: Double = StdRandom.gaussian
            val priceComputed: Double = computeMonteCarloIteration(spot, interestRate, volatility, gaussian, maturity)
            val bestPremium: Double = computePremiumForMonteCarloIteration(priceComputed, strike)
            bestPremiumsComputed += bestPremium
            i += 1
          }
        }
      }
      return bestPremiumsComputed
    }
  }

  def calculatePrice(maturity: Double, spot: Double, strike: Double, volatility: Double): Double = {
    val dayMaturity = maturity / 360
    val nbPerThreads: Long = numberOfIterations / processors

    val tasks: Array[Future[Double]] = new Array[Future[Double]](processors)

    {
      var i: Int = 0
      while (i < processors) {
        {
          val task: MonteCarloTask = new MonteCarloTask(nbPerThreads, dayMaturity, spot, strike, volatility)
          tasks(i) = pool.submit(task)
          i += 1
        }
      }
    }

    var bestPremiumsComputed: Double = 0

    {
      var i: Int = 0
      while (i < processors) {
        {
          try {
            bestPremiumsComputed += tasks(i).get
          }
          catch {
            case e: InterruptedException => {
              throw new RuntimeException(e)
            }
            case e: ExecutionException => {
              throw new RuntimeException(e)
            }
          }
          i += 1
        }
      }
    }

    // Compute mean
    val meanOfPremiums: Double = bestPremiumsComputed / (nbPerThreads * processors)

    // because the rounding might
    // might have truncate some
    // iterations
    // Discount the expected payoff at risk free interest rate
    val pricedValue: Double = scala.math.exp(-interestRate * dayMaturity) * meanOfPremiums



    return pricedValue
  }
}
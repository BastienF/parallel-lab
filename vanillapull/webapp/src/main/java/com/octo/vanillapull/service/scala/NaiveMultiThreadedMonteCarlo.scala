package com.octo.vanillapull.service.scala

import com.octo.vanillapull.util.StdRandom
import java.util.concurrent.CountDownLatch
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile(Array("naive"))
@Service
class NaiveMultiThreadedMonteCarlo extends PricingService {

  private class MonteCarloThread(iterations: Long, maturity: Double, spot: Double, strike: Double, volatility: Double, latch: CountDownLatch) extends Thread {
    private[service] var bestPremiumsComputed: Double = 0
    override def run {
      {
        var i: Long = 0
        while (i < iterations) {
          val gaussian: Double = StdRandom.gaussian
          val priceComputed: Double = computeMonteCarloIteration(spot, interestRate, volatility, gaussian, maturity)
          val bestPremium: Double = computePremiumForMonteCarloIteration(priceComputed, strike)
          bestPremiumsComputed += bestPremium
          i += 1
        }
      }
      latch.countDown
    }
  }

  def calculatePrice(maturity: Double, spot: Double, strike: Double, volatility: Double): Double = {
    val dayMaturity = maturity / 360.0
    val nbPerThreads: Long = numberOfIterations / processors
    val latch: CountDownLatch = new CountDownLatch(processors)
    val threads: Array[MonteCarloThread] = new Array[MonteCarloThread](processors)
    
    {
      var i: Int = 0
      while (i < processors) {
          val thread: MonteCarloThread = new MonteCarloThread(nbPerThreads, dayMaturity, spot, strike, volatility, latch)
          thread.setName(Thread.currentThread + " monteCarlo " + i)
          thread.start
          threads(i) = thread
          i += 1
      }
    }

    try {
      latch.await
    }
    catch {
      case e: InterruptedException => {
        throw new RuntimeException(e)
      }
    }



    var bestPremiumsComputed: Double = 0

    {
      var i: Int = 0
      while (i < processors) {
          bestPremiumsComputed += threads(i).bestPremiumsComputed
          i += 1
      }
    }
    // Compute mean
    val meanOfPremiums: Double = bestPremiumsComputed / (nbPerThreads * processors)
    // because the rounding might have truncate some iterations
    // Discount the expected payoff at risk free interest rate
    val pricedValue: Double = scala.math.exp(-interestRate * dayMaturity) * meanOfPremiums

    return pricedValue
  }
}
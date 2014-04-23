package com.octo.vanillapull.service.scala

import com.octo.vanillapull.util.StdRandom

class SMonoThreadedMonteCarlo {
  import SMonoThreadedMonteCarlo._
}

object SMonoThreadedMonteCarlo {
  def calculatePrice(maturity: Double, spot: Double, strike: Double, volatility: Double, interestRate: Double, numberOfIterations: Long): Double = {
    var bestPremiumsComputed: Double = 0
    val dayMaturity = maturity / 360.0
      var i: Long = 0
      while (i < numberOfIterations) {
          val gaussian: Double = StdRandom.gaussian
          val priceComputed: Double = computeMonteCarloIteration(spot, interestRate, volatility, gaussian, dayMaturity)
          val bestPremium: Double = computePremiumForMonteCarloIteration(priceComputed, strike)
          bestPremiumsComputed += bestPremium
          i = i + 1
        }
    val meanOfPremiums: Double = bestPremiumsComputed / numberOfIterations
    val pricedValue: Double = Math.exp(-interestRate * dayMaturity) * meanOfPremiums
    return pricedValue
  }

  def computeMonteCarloIteration(spot: Double, rate: Double, volatility: Double, gaussian: Double, maturity: Double): Double = {
    val result: Double = spot * Math.exp((rate - Math.pow(volatility, 2) * 0.5) * maturity + volatility * gaussian * Math.sqrt(maturity))
    return result
  }

  def computePremiumForMonteCarloIteration(computedBestPrice: Double, strike: Double): Double = {
    return Math.max(computedBestPrice - strike, 0)
  }
}
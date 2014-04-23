package com.octo.vanillapull.service.scala

import com.octo.vanillapull.util.StdRandom
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile(Array("mono"))
@Service
class MonoThreadedMonteCarlo extends PricingService {

  def calculatePrice(maturity: Double, spot: Double, strike: Double, volatility: Double): Double = {
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
    val pricedValue: Double = scala.math.exp(-interestRate * dayMaturity) * meanOfPremiums
    return pricedValue
  }
}
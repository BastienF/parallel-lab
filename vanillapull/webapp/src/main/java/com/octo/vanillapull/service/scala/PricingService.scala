package com.octo.vanillapull.service.scala

import javax.annotation.{PostConstruct, PreDestroy}

/**
 * Created by Bastien on 23/04/2014.
 */
trait PricingService {
  var numberOfIterations: Long = System.getProperty("iterations").toLong
  var interestRate: Double = System.getProperty("interestRate").toDouble
  val processors: Int = Runtime.getRuntime.availableProcessors

  def calculatePrice(maturity: Double, spot: Double, strike: Double, volatility: Double): Double


  def computeMonteCarloIteration(spot: Double, rate: Double, volatility: Double, gaussian: Double, maturity: Double): Double = {
    val result: Double = spot * scala.math.exp((rate - scala.math.pow(volatility, 2) * 0.5) * maturity + volatility * gaussian * scala.math.sqrt(maturity))
    return result
  }

  def computePremiumForMonteCarloIteration(computedBestPrice: Double, strike: Double): Double = {
    return scala.math.max(computedBestPrice - strike, 0)
  }

  @PostConstruct def init(): Unit = {}

  @PreDestroy def cleanUp(): Unit = {}


}

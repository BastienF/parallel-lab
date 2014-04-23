package com.octo.vanillapull.service.scala

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile(Array("mock"))
@Service
class MockMonteCarlo extends PricingService {

   def calculatePrice(maturity: Double, spot: Double, strike: Double, volatility: Double): Double = {
     return scala.math.random
   }
 }
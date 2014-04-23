package com.octo.vanillapull.service;

import com.octo.vanillapull.service.scala.SMonoThreadedMonteCarlo$;
import com.octo.vanillapull.util.StdRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author Henri Tremblay
 */
@Profile("mono")
@Service
public class MonoThreadedMonteCarlo implements PricingService {
    public final static Logger logger = LoggerFactory.getLogger(MonoThreadedMonteCarlo.class);


    long numberOfIterations = Integer.valueOf(System.getProperty("iterations"));
    @Value("${interestRate}")
    double interestRate;

    @Override
    public double calculatePrice(double maturity, double spot, double strike,
                                 double volatility) {
        return SMonoThreadedMonteCarlo$.MODULE$.calculatePrice(maturity, spot, strike, volatility, interestRate, numberOfIterations);

    }

    @PostConstruct
    public void init() throws Exception {
    }
}

package com.octo.vanillapull.service;

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
        return Math.random();
    }

    @PostConstruct
    public void init() throws Exception {
    }

    public double computeMonteCarloIteration(double spot, double rate,
                                             double volatility, double gaussian, double maturity) {
        double result = spot
                * Math.exp((rate - Math.pow(volatility, 2) * 0.5) * maturity
                + volatility * gaussian * Math.sqrt(maturity));
        return result;
    }

    public double computePremiumForMonteCarloIteration(
            double computedBestPrice, double strike) {
        return Math.max(computedBestPrice - strike, 0);
    }

}

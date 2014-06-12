package com.octo.vanillapull.service.nosynchronization;

import com.octo.vanillapull.monitoring.writers.NoSyncResultLogger;
import com.octo.vanillapull.service.BaseThreadedMonteCarlo;
import com.octo.vanillapull.service.PricingService;
import com.octo.vanillapull.util.StdRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Henri Tremblay
 */
@Profile("monoNoSync")
@Service
public class MonoThreadedMonteCarlo  extends BaseThreadedMonteCarlo {
    public final static Logger logger = LoggerFactory.getLogger(MonoThreadedMonteCarlo.class);

    private class MonoIterRunnable implements Runnable {
        public int getNumberOfIterations() {
            return numberOfIterations;
        }

        private int numberOfIterations = 0;
        private double maturity;
        private double spot;
        private double strike;
        private double volatility;

        public AtomicBoolean getShouldStop() {
            return shouldStop;
        }

        public void stop() {
            this.shouldStop.set(true);
        }

        private AtomicBoolean shouldStop = new AtomicBoolean(false);

        public double getBestPremiumsComputed() {
            return bestPremiumsComputed;
        }

        private double bestPremiumsComputed = 0;

        public MonoIterRunnable(double maturity, double spot, double strike, double volatility) {
            this.maturity = maturity;
            this.spot = spot;
            this.strike = strike;
            this.volatility = volatility;
        }

        @Override
            public void run() {
                while (!shouldStop.get()) {
                    double gaussian = StdRandom.gaussian();
                    double priceComputed = computeMonteCarloIteration(spot,
                           interestRate, volatility, gaussian, maturity);
                    double bestPremium = computePremiumForMonteCarloIteration(
                          priceComputed, strike);
                    bestPremiumsComputed += bestPremium;
                    numberOfIterations++;
                }
            }


    }

    @Autowired
    private NoSyncResultLogger resultLogger;
    long delayToStop = Long.valueOf(System.getProperty("noSynchronization_delay"));

    @Override
    public double calculatePrice(double maturity, double spot, double strike,
                                 double volatility) {


        maturity /= 360.0;

        MonoIterRunnable target = new MonoIterRunnable(maturity, spot, strike, volatility);
        new Thread(target).start();

        try {
            Thread.sleep(delayToStop);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        target.stop();

        // Compute mean
        double meanOfPremiums = target.getBestPremiumsComputed() / target.getNumberOfIterations();

        // Discount the expected payoff at risk free interest rate
        double pricedValue = Math.exp(-interestRate * maturity)
                * meanOfPremiums;

        resultLogger.write(target.getNumberOfIterations());
        return pricedValue;
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

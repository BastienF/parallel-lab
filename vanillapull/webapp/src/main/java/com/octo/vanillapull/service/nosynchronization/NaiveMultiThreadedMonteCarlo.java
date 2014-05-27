package com.octo.vanillapull.service.nosynchronization;

import com.octo.vanillapull.monitoring.writers.NoSyncResultLogger;
import com.octo.vanillapull.service.PricingService;
import com.octo.vanillapull.util.StdRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Henri Tremblay
 */
@Profile("naiveNoSync")
@Service
public class NaiveMultiThreadedMonteCarlo implements PricingService {

	private class MonteCarloThread extends Thread {

		private double maturity, spot, strike, volatility;
		double bestPremiumsComputed = 0;
        private AtomicBoolean shouldStop = new AtomicBoolean(false);
        private int numberOfIterations = 0;

		public MonteCarloThread(double maturity,
				double spot, double strike, double volatility) {
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

        public void stopThread() {
            shouldStop.set(true);
        }

        public int getNumberOfIterations() {
            return numberOfIterations;
        }
	}


    @Autowired
    private NoSyncResultLogger resultLogger;

    long delayToStop = Long.valueOf(System.getProperty("noSynchronization_delay"));
	@Value("${interestRate}")
	double interestRate;

	private final int processors = Runtime.getRuntime().availableProcessors();

	@Override
	public double calculatePrice(double maturity, double spot, double strike,
			double volatility) {

		maturity /= 360.0;


		MonteCarloThread[] threads = new MonteCarloThread[processors];
		for (int i = 0; i < processors; i++) {
			MonteCarloThread thread = new MonteCarloThread(maturity, spot, strike, volatility);
			thread.setName(Thread.currentThread() + " monteCarlo " + i);
			thread.start();
			threads[i] = thread;
		}


        try {
            Thread.sleep(delayToStop);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

		double bestPremiumsComputed = 0;
        int numberOfIterations = 0;

		for (int i = 0; i < processors; i++) {
            threads[i].stopThread();
            numberOfIterations += threads[i].getNumberOfIterations();
			bestPremiumsComputed += threads[i].bestPremiumsComputed;
		}

		// Compute mean
		double meanOfPremiums = bestPremiumsComputed
				/ (numberOfIterations); // not using numberOfIterations
												// because the rounding might
												// might have truncate some
												// iterations

		// Discount the expected payoff at risk free interest rate
		double pricedValue = Math.exp(-interestRate * maturity)
				* meanOfPremiums;


        resultLogger.write(numberOfIterations);
		return pricedValue;
	}

    @Override
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

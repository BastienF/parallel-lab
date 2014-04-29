package com.octo.vanillapull.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

/**
 * @author Henri Tremblay
 */
@Profile("naive")
@Service
public class NaiveMultiThreadedMonteCarlo implements PricingService {

	private class MonteCarloThread extends Thread {

		private final long nbIterations;
		private double maturity, spot, strike, volatility;
		double bestPremiumsComputed = 0;
		private CountDownLatch latch;

		public MonteCarloThread(long nbIterations, double maturity,
				double spot, double strike, double volatility,
				CountDownLatch latch) {
			this.nbIterations = nbIterations;
			this.maturity = maturity;
			this.spot = spot;
			this.strike = strike;
			this.volatility = volatility;
			this.latch = latch;
		}

		@Override
		public void run() {
			bestPremiumsComputed = Math.random();
			latch.countDown();
		}
	}


    long numberOfIterations = Integer.valueOf(System.getProperty("iterations"));
	@Value("${interestRate}")
	double interestRate;

	private final int processors = Runtime.getRuntime().availableProcessors();

	@Override
	public double calculatePrice(double maturity, double spot, double strike,
			double volatility) {

		maturity /= 360.0;

		long nbPerThreads = numberOfIterations / processors;

		CountDownLatch latch = new CountDownLatch(processors);

		MonteCarloThread[] threads = new MonteCarloThread[processors];
		for (int i = 0; i < processors; i++) {
			MonteCarloThread thread = new MonteCarloThread(nbPerThreads,
					maturity, spot, strike, volatility, latch);
			thread.setName(Thread.currentThread() + " monteCarlo " + i);
			thread.start();
			threads[i] = thread;
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		double bestPremiumsComputed = 0;

		for (int i = 0; i < processors; i++) {
			bestPremiumsComputed += threads[i].bestPremiumsComputed;
		}

		return Math.random();
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

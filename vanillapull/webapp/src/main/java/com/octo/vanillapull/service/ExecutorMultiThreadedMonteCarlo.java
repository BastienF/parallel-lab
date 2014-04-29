package com.octo.vanillapull.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;

/**
 * @author Henri Tremblay
 */
@Profile("executor")
@Service
public class ExecutorMultiThreadedMonteCarlo implements PricingService {

	private class MonteCarloTask implements Callable<Double> {

		private final long nbIterations;
		private double maturity, spot, strike, volatility;
		double bestPremiumsComputed = 0;

		public MonteCarloTask(long nbIterations, double maturity, double spot,
				double strike, double volatility) {
			this.nbIterations = nbIterations;
			this.maturity = maturity;
			this.spot = spot;
			this.strike = strike;
			this.volatility = volatility;
		}

		@Override
		public Double call() {
			return Math.random();
		}
	}

	long numberOfIterations = Integer.valueOf(System.getProperty("iterations"));
	@Value("${interestRate}")
	double interestRate;

	private final int processors = Runtime.getRuntime().availableProcessors();
	private ExecutorService pool;

	@PostConstruct
	public void init() throws Exception {
		pool = Executors.newFixedThreadPool(processors);
	}

	@PreDestroy
	public void cleanUp() throws Exception {
		pool.shutdown();
	}

	@Override
	public double calculatePrice(double maturity, double spot, double strike,
			double volatility) {

		maturity /= 360.0;

		long nbPerThreads = numberOfIterations / processors;

		@SuppressWarnings("unchecked")
		Future<Double>[] tasks = new Future[processors];
		for (int i = 0; i < processors; i++) {
			MonteCarloTask task = new MonteCarloTask(nbPerThreads, maturity,
					spot, strike, volatility);
			tasks[i] = pool.submit(task);
		}

		double bestPremiumsComputed = 0;

		for (int i = 0; i < processors; i++) {
			try {
				bestPremiumsComputed += tasks[i].get();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
		}

		return Math.random();
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

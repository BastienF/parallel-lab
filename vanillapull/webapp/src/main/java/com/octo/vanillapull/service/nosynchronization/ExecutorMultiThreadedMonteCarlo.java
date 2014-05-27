package com.octo.vanillapull.service.nosynchronization;

import com.octo.vanillapull.monitoring.writers.NoSyncResultLogger;
import com.octo.vanillapull.service.PricingService;
import com.octo.vanillapull.util.StdRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Henri Tremblay
 */
@Profile("executorNoSync")
@Service
public class ExecutorMultiThreadedMonteCarlo implements PricingService {

	private class MonteCarloTask implements Callable<Double> {

		private double maturity, spot, strike, volatility;
		double bestPremiumsComputed = 0;
        private AtomicBoolean shouldStop = new AtomicBoolean(false);
        private int numberOfIterations = 0;

		public MonteCarloTask(double maturity, double spot,
				double strike, double volatility) {
			this.maturity = maturity;
			this.spot = spot;
			this.strike = strike;
			this.volatility = volatility;
		}

		@Override
		public Double call() {
			while (!shouldStop.get()) {
				double gaussian = StdRandom.gaussian();
				double priceComputed = computeMonteCarloIteration(spot,
						interestRate, volatility, gaussian, maturity);
				double bestPremium = computePremiumForMonteCarloIteration(
						priceComputed, strike);
				bestPremiumsComputed += bestPremium;
                numberOfIterations++;
			}
			return bestPremiumsComputed;
		}

        public int getNumberOfIterations() {
            return numberOfIterations;
        }

        public void stop() {
            shouldStop.set(true);
        }
	}

    @Autowired
    private NoSyncResultLogger resultLogger;

	long delayToStop = Long.valueOf(System.getProperty("noSynchronization_delay"));
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

		@SuppressWarnings("unchecked")
		Future<Double>[] tasks = new Future[processors];
        MonteCarloTask[] monteCarloTasks = new MonteCarloTask[processors];
		for (int i = 0; i < processors; i++) {
			MonteCarloTask task = new MonteCarloTask(maturity,
					spot, strike, volatility);
            monteCarloTasks[i] = task;
			tasks[i] = pool.submit(task);
		}

		double bestPremiumsComputed = 0;

        try {
            Thread.sleep(delayToStop);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int numberOfIterations = 0;

        for (int i = 0; i < processors; i++) {
            monteCarloTasks[i].stop();
            try {
                bestPremiumsComputed += tasks[i].get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            numberOfIterations += monteCarloTasks[i].getNumberOfIterations();
        }

		// Compute mean
		double meanOfPremiums = bestPremiumsComputed
				/ (processors); // not using numberOfIterations
												// because the rounding might
												// might have truncate some
												// iterations

		// Discount the expected payoff at risk free interest rate
		double pricedValue = Math.exp(-interestRate * maturity)
				* meanOfPremiums;

        resultLogger.write(numberOfIterations);

		return pricedValue;
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

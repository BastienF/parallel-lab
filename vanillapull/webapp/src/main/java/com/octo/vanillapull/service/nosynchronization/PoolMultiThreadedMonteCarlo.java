package com.octo.vanillapull.service.nosynchronization;

import com.octo.vanillapull.monitoring.writers.NoSyncResultLogger;
import com.octo.vanillapull.service.BaseThreadedMonteCarlo;
import com.octo.vanillapull.service.PricingService;
import com.octo.vanillapull.util.StdRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Henri Tremblay
 */
@Profile("poolNoSync")
@Service
public class PoolMultiThreadedMonteCarlo  extends BaseThreadedMonteCarlo {

	private class MonteCarloTask extends ForkJoinTask<Double> {

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
		public boolean exec() {
			while (!shouldStop.get()) {
				double gaussian = StdRandom.gaussian();
				double priceComputed = computeMonteCarloIteration(spot,
						interestRate, volatility, gaussian, maturity);
				double bestPremium = computePremiumForMonteCarloIteration(
						priceComputed, strike);
				bestPremiumsComputed += bestPremium;
                numberOfIterations++;
			}
			return true;
		}

        public void stop(){
            shouldStop.set(true);
        }

        public int getNumberOfIterations() {
            return numberOfIterations;
        }

		@Override
		public Double getRawResult() {
			return bestPremiumsComputed;
		}

		@Override
		protected void setRawResult(Double value) {
			this.bestPremiumsComputed = value;
		}
	}



    @Autowired
    private NoSyncResultLogger resultLogger;

    long delayToStop = Long.valueOf(System.getProperty("noSynchronization_delay"));

	private ForkJoinPool pool;

	@PostConstruct
	public void init() throws Exception {
        boolean asyncMode = Boolean.getBoolean("asyncMode");
		pool = new ForkJoinPool(processors, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, asyncMode);
	}

	@PreDestroy
	public void cleanUp() throws Exception {
		pool.shutdown();
	}

	@Override
	public double calculatePrice(double maturity, double spot, double strike,
			double volatility) {

		maturity /= 360.0;

		MonteCarloTask[] tasks = new MonteCarloTask[processors];
		for (int i = 0; i < processors; i++) {
			MonteCarloTask task = new MonteCarloTask(maturity,
					spot, strike, volatility);
			pool.execute(task);
			tasks[i] = task;
		}

		double bestPremiumsComputed = 0;

        try {
            Thread.sleep(delayToStop);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        int numberOfIterations = 0;

        for (int i = 0; i < processors; i++) {
            tasks[i].stop();
			try {
				bestPremiumsComputed += tasks[i].get();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
            numberOfIterations += tasks[i].getNumberOfIterations();
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

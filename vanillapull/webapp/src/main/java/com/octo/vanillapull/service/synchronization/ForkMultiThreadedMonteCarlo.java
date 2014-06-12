package com.octo.vanillapull.service.synchronization;

import com.octo.vanillapull.service.BaseThreadedMonteCarlo;
import com.octo.vanillapull.util.StdRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author Henri Tremblay
 */
@Profile("fork")
@Service
public class ForkMultiThreadedMonteCarlo extends BaseThreadedMonteCarlo {

    private final static Logger logger = LoggerFactory.getLogger(ForkMultiThreadedMonteCarlo.class);

    private class MasterMonteCarloTask extends RecursiveTask<Double> {

        private final double maturity, spot, strike, volatility;

        public MasterMonteCarloTask(double maturity, double spot, double strike, double volatility) {
            this.maturity = maturity;
            this.spot = spot;
            this.strike = strike;
            this.volatility = volatility;
        }

        @Override
        protected Double compute() {
            int nbPerThreads = getNbThreads();

            MonteCarloTask[] tasks = new MonteCarloTask[processors];
            for (int i = 0; i < processors; i++) {
                MonteCarloTask task = new MonteCarloTask(nbPerThreads, maturity,
                        spot, strike, volatility);
                task.fork();
                tasks[i] = task;
            }

            double bestPremiumsComputed = 0;

            for (int i = 0; i < processors; i++) {
                bestPremiumsComputed += tasks[i].join();
            }

            // Compute mean
            double meanOfPremiums = bestPremiumsComputed / (nbPerThreads * processors); // not using numberOfIterations
            // because the rounding might have truncate some iterations

            // Discount the expected payoff at risk free interest rate
            double pricedValue = Math.exp(-interestRate * maturity) * meanOfPremiums;
            return pricedValue;
        }
    }

	private class MonteCarloTask extends RecursiveTask<Double> {

		private final int nbIterations;
		private double maturity, spot, strike, volatility;

		public MonteCarloTask(int nbIterations, double maturity, double spot,
				double strike, double volatility) {
			this.nbIterations = nbIterations;
			this.maturity = maturity;
			this.spot = spot;
			this.strike = strike;
			this.volatility = volatility;
		}

		@Override
		public Double compute() {
            double bestPremiumsComputed = 0;
			for (long i = 0; i < nbIterations; i++) {
				double gaussian = StdRandom.gaussian();
				double priceComputed = computeMonteCarloIteration(spot,
						interestRate, volatility, gaussian, maturity);
				double bestPremium = computePremiumForMonteCarloIteration(
						priceComputed, strike);
				bestPremiumsComputed += bestPremium;
			}
			return bestPremiumsComputed;
		}
	}

	private ForkJoinPool pool;

	@PostConstruct
	public void init() {
        boolean asyncMode = Boolean.getBoolean("asyncMode");
        pool = new ForkJoinPool(processors, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, asyncMode);
        logger.info("Launch pool with parallelism {} and async mode {}", pool.getParallelism(), pool.getAsyncMode());
	}

	@PreDestroy
	public void cleanUp() {
		pool.shutdown();
	}

	@Override
	public double calculatePrice(double maturity, double spot, double strike,
			double volatility) {

		maturity /= 360.0;

        MasterMonteCarloTask task = new MasterMonteCarloTask(maturity, spot, strike, volatility);

        double pricedValue = pool.invoke(task);

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

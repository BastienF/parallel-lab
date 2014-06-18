package com.octo.vanillapull.service.synchronization;

import com.octo.vanillapull.monitoring.meters.ChronicleLogger;
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

/**
 * @author Henri Tremblay
 */
@Profile("pool")
@Service
public class PoolMultiThreadedMonteCarlo extends BaseThreadedMonteCarlo {
    public final static Logger logger = LoggerFactory.getLogger(PoolMultiThreadedMonteCarlo.class);

    private class MonteCarloTask extends ForkJoinTask<Double> implements ChronicleLogger.ChronicleLoggable {

        private final String name;
        private final String color;

        private final long nbIterations;
        private double maturity, spot, strike, volatility;
        double bestPremiumsComputed = 0;

        public MonteCarloTask(long nbIterations, double maturity, double spot,
                              double strike, double volatility, String name, String color) {
            this.nbIterations = nbIterations;
            this.maturity = maturity;
            this.spot = spot;
            this.strike = strike;
            this.volatility = volatility;
            this.name = name;
            this.color = color;
        }

        @Override
        public boolean exec() {
            if (logWriter.isLogActived())
                chronicleLogger.logEntry(this, getBehaviorLogEntry("Start", name, color));
            for (long i = 0; i < nbIterations; i++) {
                double gaussian = StdRandom.gaussian();
                double priceComputed = computeMonteCarloIteration(spot,
                        interestRate, volatility, gaussian, maturity);
                double bestPremium = computePremiumForMonteCarloIteration(
                        priceComputed, strike);
                bestPremiumsComputed += bestPremium;
            }
            if (logWriter.isLogActived())
                chronicleLogger.logEntry(this, getBehaviorLogEntry("Stop", name, color));
            return true;
        }

        @Override
        public Double getRawResult() {
            return bestPremiumsComputed;
        }

        @Override
        protected void setRawResult(Double value) {
            this.bestPremiumsComputed = value;
        }

        @Override
        public String getChronicleName() {
            return name + Long.valueOf(initTime);
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
        final Integer requestId = requestNumber.getAndIncrement();
        final String requestName = "Request-" + requestId.toString();
        ChronicleLogger.ChronicleLoggable request = new ChronicleLogger.ChronicleLoggable() {
            @Override
            public String getChronicleName() {

                return requestName + Long.valueOf(initTime);
            }
        };
        if (logWriter.isLogActived())
            chronicleLogger.logEntry(request, getBehaviorLogEntry("Start", requestName, getRequestColor(requestId)));
        maturity /= 360.0;

        int nbPerThreads = getNbThreads();

        MonteCarloTask[] tasks = new MonteCarloTask[processors];
        for (int i = 0; i < processors; i++) {
            MonteCarloTask task = new MonteCarloTask(nbPerThreads, maturity,
                    spot, strike, volatility, "Task-" + requestId + "-" + i, getRequestColor(requestId));
            pool.execute(task);
            tasks[i] = task;
        }

        double bestPremiumsComputed = 0;

        for (int i = 0; i < processors; i++) {
            try {
                MonteCarloTask task = tasks[i];
                bestPremiumsComputed += task.get();
                if (logWriter.isLogActived())
                    logWriter.log(chronicleLogger.getAllEntriesAndClose(task));

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        // Compute mean
        double meanOfPremiums = bestPremiumsComputed
                / (nbPerThreads * processors); // not using numberOfIterations
        // because the rounding might
        // might have truncate some
        // iterations

        // Discount the expected payoff at risk free interest rate
        double pricedValue = Math.exp(-interestRate * maturity)
                * meanOfPremiums;
        if (logWriter.isLogActived()) {
            chronicleLogger.logEntry(request, getBehaviorLogEntry("Stop", requestName, getRequestColor(requestId)));
            logWriter.log(chronicleLogger.getAllEntriesAndClose(request));
        }
        return pricedValue;
    }

    public double computeMonteCarloIteration(double spot, double rate,
                                             double volatility, double gaussian, double maturity) {
        return spot
                * Math.exp(((rate - (Math.pow(volatility, 2) * 0.5)) * maturity)
                + (volatility * gaussian * Math.sqrt(maturity)));
    }

    public double computePremiumForMonteCarloIteration(
            double computedBestPrice, double strike) {
        return Math.max(computedBestPrice - strike, 0);
    }
}

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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author Henri Tremblay
 */
@Profile("fork")
@Service
public class ForkMultiThreadedMonteCarlo extends BaseThreadedMonteCarlo {

    private final static Logger logger = LoggerFactory.getLogger(ForkMultiThreadedMonteCarlo.class);

    private class MasterMonteCarloTask extends RecursiveTask<Double> implements ChronicleLogger.ChronicleLoggable {

        private final double maturity, spot, strike, volatility;
        private final int requestId;
        private final String color;
        private final String name;

        public MasterMonteCarloTask(double maturity, double spot, double strike, double volatility, int requestId, String color) {
            this.maturity = maturity;
            this.spot = spot;
            this.strike = strike;
            this.volatility = volatility;
            this.requestId = requestId;
            this.color = color;
            this.name = "MasterTask-" + requestId;
        }

        @Override
        protected Double compute() {
            if (logWriter.isLogActived())
                chronicleLogger.logEntry(this, getBehaviorLogEntry("Start", name, color));
            int nbPerThreads = getNbThreads();
            MonteCarloTask[] tasks = new MonteCarloTask[processors];
            double bestPremiumsComputed = 0;
            for (int i = 0; i < processors; i++) {
                MonteCarloTask task = new MonteCarloTask(nbPerThreads, maturity,
                        spot, strike, volatility, "Task-" + requestId + "-" + i, color);
                if (i < processors - 1)
                    task.fork();
                else
                    bestPremiumsComputed += task.compute();
                tasks[i] = task;
            }
            for (int i = 0; i < processors; i++) {
                final MonteCarloTask task = tasks[i];

                if (i < processors - 1) {
                    bestPremiumsComputed += task.join();
                    if (logWriter.isLogActived())
                        logWriter.log(chronicleLogger.getAllEntriesAndClose(task));
                } else {
                    if (logWriter.isLogActived())
                        chronicleLogger.getAllEntriesAndClose(task);
                }
            }
            // Compute mean
            double meanOfPremiums = bestPremiumsComputed / (nbPerThreads * processors); // not using numberOfIterations
            // because the rounding might have truncate some iterations
            // Discount the expected payoff at risk free interest rate
            double pricedValue = Math.exp(-interestRate * maturity) * meanOfPremiums;

            if (logWriter.isLogActived()) {
                chronicleLogger.logEntry(this, getBehaviorLogEntry("Stop", name, color));
                logWriter.log(chronicleLogger.getAllEntriesAndClose(this));
            }
            return pricedValue;
        }

        @Override
        public String getChronicleName() {
            return name + Long.valueOf(initTime);
        }
    }

    private class MonteCarloTask extends RecursiveTask<Double> implements ChronicleLogger.ChronicleLoggable {

        private final String name;
        private final String color;

        private final int nbIterations;
        private double maturity, spot, strike, volatility;

        public MonteCarloTask(int nbIterations, double maturity, double spot,
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
        public Double compute() {
            if (logWriter.isLogActived())
                chronicleLogger.logEntry(this, getBehaviorLogEntry("Start", name, color));
            double bestPremiumsComputed = 0;
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
            return bestPremiumsComputed;
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

        int requestId = requestNumber.getAndIncrement();
        final String requestName = "Request-" + requestId;
        ChronicleLogger.ChronicleLoggable request = new ChronicleLogger.ChronicleLoggable() {
            @Override
            public String getChronicleName() {
                return requestName + Long.valueOf(initTime);
            }
        };

        final String requestColor = getRequestColor(requestId);
        if (logWriter.isLogActived())
            chronicleLogger.logEntry(request, getBehaviorLogEntry("Start", requestName, requestColor));

        maturity /= 360.0;

        MasterMonteCarloTask task = new MasterMonteCarloTask(maturity, spot, strike, volatility, requestId, requestColor);

        double pricedValue = pool.invoke(task);
        if (logWriter.isLogActived()) {
            chronicleLogger.logEntry(request, getBehaviorLogEntry("Stop", requestName, requestColor));
            final String allEntries = chronicleLogger.getAllEntriesAndClose(request);
            logWriter.log(allEntries);
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

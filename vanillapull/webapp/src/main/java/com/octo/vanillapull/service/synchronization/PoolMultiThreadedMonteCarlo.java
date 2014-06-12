package com.octo.vanillapull.service.synchronization;

import com.octo.vanillapull.monitoring.writers.FJPoolBehaviorLogWriter;
import com.octo.vanillapull.service.PricingService;
import com.octo.vanillapull.util.StdRandom;
import net.openhft.chronicle.ExcerptAppender;
import net.openhft.chronicle.ExcerptTailer;
import net.openhft.chronicle.IndexedChronicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Henri Tremblay
 */
@Profile("pool")
@Service
public class PoolMultiThreadedMonteCarlo implements PricingService {
    public final static Logger logger = LoggerFactory.getLogger(PoolMultiThreadedMonteCarlo.class);
    private final static String[] colors = {"aqua", "blue", "fuchsia", "gray", "green", "lime", "maroon", "olive", "purple", "red", "silver", "teal", "yellow"};
    private static AtomicInteger requestNumber = new AtomicInteger(0);
    private final static long initTime = System.nanoTime();

    private class MonteCarloTask extends ForkJoinTask<Double> {

        /* FJPool behavior handling */
        private final String name;
        private final String color;
        private final IndexedChronicle chronicle;

        public IndexedChronicle getChronicle() {
            return chronicle;
        }
        /*-------------------*/

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
            String basePath = new StringBuilder().append(System.getProperty("java.io.tmpdir")).append("/SimpleChronicle").append(name).append(initTime).toString();
            try {
                chronicle = new IndexedChronicle(basePath);
            } catch (FileNotFoundException e) {
                logger.error("Chronicles logging error : " + e.getMessage());
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean exec() {
            ExcerptAppender appender = null;
            try {
                try {
                    appender = chronicle.createAppender();
                } catch (IOException e) {
                    logger.error("Chronicles create appender error : " + e.getMessage());
                    throw new RuntimeException(e);
                }
                appender.startExcerpt(800); // an upper limit to how much space in bytes this message should need.
                appender.writeObject(getFJPoolBehaviorLogEntry("Start"));
                for (long i = 0; i < nbIterations; i++) {
                    double gaussian = StdRandom.gaussian();
                    double priceComputed = computeMonteCarloIteration(spot,
                            interestRate, volatility, gaussian, maturity);
                    double bestPremium = computePremiumForMonteCarloIteration(
                            priceComputed, strike);
                    bestPremiumsComputed += bestPremium;
                }
                return true;
            } finally {
                appender.startExcerpt(800); // an upper limit to how much space in bytes this message should need.
                appender.writeObject(getFJPoolBehaviorLogEntry("Stop"));
                appender.finish();
            }
        }

        @Override
        public Double getRawResult() {
            return bestPremiumsComputed;
        }

        @Override
        protected void setRawResult(Double value) {
            this.bestPremiumsComputed = value;
        }

        private String getFJPoolBehaviorLogEntry(String status) {
            return new StringBuilder().append("{\n\"N\": \"").append(name).append("\",\n\"Th\": \"").append(Thread.currentThread().getName()).append("\",\n\"C\": \"").append(color).append("\",\n\"S\": \"").append(status).append("\",\n\"T\": \"").append((System.nanoTime() - initTime) / 1000000).append("\"\n}").toString();
        }
    }


    long numberOfIterations = Integer.valueOf(System.getProperty("iterations"));
    @Value("${interestRate}")
    double interestRate;

    @Autowired
    FJPoolBehaviorLogWriter logWriter;

    private final int processors = Runtime.getRuntime().availableProcessors();
    private ForkJoinPool pool;

    @PostConstruct
    public void init() throws Exception {
        pool = new ForkJoinPool();
    }

    @PreDestroy
    public void cleanUp() throws Exception {
        pool.shutdown();
    }

    private String getFJPoolBehaviorLogEntry(String status, String requestName, String requestColor) {
        return new StringBuilder().append("{\n\"N\": \"").append(requestName).append("\",\n\"Th\": \"").append(Thread.currentThread().getName()).append("\",\n\"C\": \"").append(requestColor).append("\",\n\"S\": \"").append(status).append("\",\n\"T\": \"").append((System.nanoTime() - initTime) / 1000000).append("\"\n}").toString();
    }

    @Override
    public double calculatePrice(double maturity, double spot, double strike,
                                 double volatility) {
            int requestId = requestNumber.getAndIncrement();
            String requestName = new StringBuilder().append("Request-").append(requestId).toString();

            String basePath = new StringBuilder().append(System.getProperty("java.io.tmpdir")).append("/SimpleChronicle").append(requestName).append(initTime).toString();
            IndexedChronicle chronicle = null;
            try {
                chronicle = new IndexedChronicle(basePath);
            } catch (FileNotFoundException e) {
                logger.error("Chronicles logging error : " + e.getMessage());
                throw new RuntimeException(e);
            }
            ExcerptAppender appender = null;
            try {
                appender = chronicle.createAppender();
            } catch (IOException e) {
                logger.error("Chronicles create appender error : " + e.getMessage());
                throw new RuntimeException(e);
            }
        final String requestColor = colors[requestId % (colors.length)];
        double pricedValue = 0;
        try {

            appender.startExcerpt(800); // an upper limit to how much space in bytes this message should need.
            appender.writeObject(getFJPoolBehaviorLogEntry("Start", requestName, requestColor));

            maturity /= 360.0;

            long nbPerThreads = numberOfIterations / processors;

            MonteCarloTask[] tasks = new MonteCarloTask[processors];
            for (int i = 0; i < processors; i++) {
                MonteCarloTask task = new MonteCarloTask(nbPerThreads, maturity,
                        spot, strike, volatility, new StringBuilder().append("Task-").append(requestId).append("-").append(i).toString(), requestColor);
                pool.execute(task);
                tasks[i] = task;
            }

            double bestPremiumsComputed = 0;

            StringBuilder taskLog = new StringBuilder();
            for (int i = 0; i < processors; i++) {
                try {
                    MonteCarloTask task = tasks[i];
                    bestPremiumsComputed += task.get();
                    IndexedChronicle chronicleT = task.getChronicle();
                    ExcerptTailer reader = chronicleT.createTailer();
                    boolean loop = true;
                    while (loop) {
                        loop = reader.nextIndex();
                        Object ret = reader.readObject();
                        taskLog.append(ret.toString() + ",\n");

                    }
                    reader.finish();
                    chronicleT.close();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            logWriter.log(taskLog.toString());


            // Compute mean
            double meanOfPremiums = bestPremiumsComputed
                    / (nbPerThreads * processors); // not using numberOfIterations
            // because the rounding might
            // might have truncate some
            // iterations

            // Discount the expected payoff at risk free interest rate
            pricedValue = Math.exp(-interestRate * maturity)
                    * meanOfPremiums;
        }
        finally {
            appender.startExcerpt(800); // an upper limit to how much space in bytes this message should need.
            appender.writeObject(getFJPoolBehaviorLogEntry("Stop", requestName, requestColor));
            appender.finish();

            ExcerptTailer reader = null;
            try {
                reader = chronicle.createTailer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            StringBuilder requestLog = new StringBuilder();
            boolean loop = true;
            while (loop) {
                loop = reader.nextIndex();
                Object ret = reader.readObject();
                requestLog.append(ret.toString() + ",\n");
            }
            reader.finish();
            try {
                chronicle.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            logWriter.log(requestLog.toString());
        }
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

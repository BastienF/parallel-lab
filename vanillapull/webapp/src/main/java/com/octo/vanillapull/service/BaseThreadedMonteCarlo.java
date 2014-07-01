package com.octo.vanillapull.service;

import com.octo.vanillapull.monitoring.meters.ChronicleLogger;
import com.octo.vanillapull.monitoring.writers.FJPoolBehaviorLogWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Henri on 12/06/2014.
 */
public abstract class BaseThreadedMonteCarlo implements PricingService {
    public final static Logger logger = LoggerFactory.getLogger(BaseThreadedMonteCarlo.class);

    public static final int processors = Runtime.getRuntime().availableProcessors();
    protected final static String[] colors = {"aqua", "blue", "fuchsia", "gray", "green", "lime", "maroon", "olive", "purple", "red", "silver", "teal", "yellow"};
    protected static AtomicInteger requestNumber = new AtomicInteger(0);
    protected final static long initTime = System.nanoTime();


    @Autowired
    public FJPoolBehaviorLogWriter logWriter;

    @Autowired
    public ChronicleLogger chronicleLogger;
    public int numberOfIterations = Integer.getInteger("iterations", 0);

    @Value("${interestRate}")
    public double interestRate;

    public void init() {}

    public void cleanUp() {}


    public String getRequestColor(Integer requestId) {
        return colors[requestId % (colors.length)];
    }

    protected String getBehaviorLogEntry(String status, String name, String color) {
        return new StringBuilder().append("{\n\"N\": \"").append(name).append("\",\n\"Th\": \"").append(Thread.currentThread().getName()).append("\",\n\"C\": \"").append(color).append("\",\n\"S\": \"").append(status).append("\",\n\"T\": \"").append((System.nanoTime() - initTime) / 1000000).append("\"\n}").toString();
    }

    protected int getNbThreads() {
        int result = numberOfIterations / processors;
        if(result * processors != numberOfIterations) {
            logger.error(numberOfIterations + " can't be divided by " + processors);
        }
        return result;
    }
}

package com.octo.vanillapull.monitoring.meters;

import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Bastien on 12/03/2014.
 */

public class MetersManager {
    public final static Logger logger = LoggerFactory.getLogger(MetersManager.class);
    private final static MetricRegistry metrics = new MetricRegistry();
    private final static AtomicLong totalTime = new AtomicLong(0);
    private final static AtomicInteger totalRequests = new AtomicInteger(0);


    static public MetricRegistry getMetrics() {
        return metrics;
    }

    static public void addRequest(long duration) {
        totalTime.addAndGet(duration);
        totalRequests.incrementAndGet();
    }

    static public long getTotalTime () {
        return totalTime.get();
    }

    static public int getRequests () {
        return totalRequests.get();
    }
}
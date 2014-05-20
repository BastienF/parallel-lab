package com.octo.vanillapull.monitoring.writers;

import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

/**
 * Created by Bastien on 10/04/2014.
 */
@Component
public class MetricsLogger {
    public final static Logger logger = LoggerFactory.getLogger(MetricsLogger.class);


    @Autowired private LogWriter writerActor;

    private final String implementation;
    private final String iterations;
    private final String users;
    private final long start;
    private int initContextSwitching = -1;
    private Integer lastContextSwitching = null;

    public MetricsLogger() {
        implementation = System.getProperty("implementation");
        iterations = System.getProperty("iterations");
        users = System.getProperty("users");
        start = System.currentTimeMillis();
    }

    public void start() {
    }

    public void addContextSwitching (int contextSwitching) {
        if (lastContextSwitching == null) {
            initContextSwitching = contextSwitching;
        }
        lastContextSwitching = contextSwitching - initContextSwitching;
    }

    @Scheduled(fixedDelay = 2000)
    public void write() {
        String nbThreadValue = Integer.toString(Thread.getAllStackTraces().keySet().size());
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        String processCpuLoad = Double.toString(osBean.getProcessCpuLoad() * 100);

        String systemAvgLoadValue = Double.toString(osBean.getSystemLoadAverage());
        Runtime runtime = Runtime.getRuntime();
        String heapMemoryUsageValue = Long.toString((runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024));

        StringBuilder entry = new StringBuilder("{\n\"t\": \"metric\",\n");
        entry.append("\"Implementation\": \"").append(implementation).append("\",\n");
        entry.append("\"Iterations\": \"").append(iterations).append("\",\n");
        entry.append("\"Users\": \"").append(users).append("\",\n");
        entry.append("\"ThreadCount\": \"").append(nbThreadValue).append("\",\n");
        entry.append("\"ProcessCpuLoad (%)\": \"").append(processCpuLoad).append("\",\n");
        entry.append("\"SystemLoadAverage\": \"").append(systemAvgLoadValue).append("\",\n");
        entry.append("\"HeapMemoryUsage (Mo)\": \"").append(heapMemoryUsageValue).append("\",\n");
        if (lastContextSwitching != null)
            entry.append("\"ContextSwitching\": \"").append(lastContextSwitching.toString()).append("\",\n");
        entry.append("\"D\": \"").append(System.currentTimeMillis() - start).append("\"\n");
        entry.append("},\n");
        writerActor.log(entry.toString());
    }
}
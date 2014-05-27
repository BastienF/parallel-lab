package com.octo.vanillapull.monitoring.writers;

import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bastien on 10/04/2014.
 */
@Component
public class MetricsLogger extends AMonteCarloLogger {
    public final static Logger logger = LoggerFactory.getLogger(MetricsLogger.class);



    private final long start;
    private int initContextSwitching = -1;
    private Integer lastContextSwitching = null;

    public MetricsLogger() {
        start = System.currentTimeMillis();
    }

    public void start() {
    }

    public void addContextSwitching (int contextSwitching) {
        if (writerActor.isStarted()) {
            if (lastContextSwitching == null) {
                initContextSwitching = contextSwitching;
            }
            lastContextSwitching = contextSwitching - initContextSwitching;
        }
    }

    @Scheduled(fixedDelay = 2000)
    public void write() {
        String nbThreadValue = Integer.toString(Thread.getAllStackTraces().keySet().size());
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        String processCpuLoad = Long.toString(Math.round(osBean.getProcessCpuLoad() * 100));

        String systemAvgLoadValue = Double.toString(osBean.getSystemLoadAverage());
        Runtime runtime = Runtime.getRuntime();
        String heapMemoryUsageValue = Long.toString((runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024));

        Map<String, String> metricMap = new HashMap<>(7);
        metricMap.put("t", "metric");
        metricMap.put("ThreadCount", nbThreadValue);
        metricMap.put("ProcessCpuLoad (%)", processCpuLoad);
        metricMap.put("SystemLoadAverage", systemAvgLoadValue);
        metricMap.put("HeapMemoryUsage (Mo)", heapMemoryUsageValue);
        if (lastContextSwitching != null)
            metricMap.put("ContextSwitching", lastContextSwitching.toString());
        metricMap.put("D", Long.toString(System.currentTimeMillis() - start));
        logWrapper(metricMap);
    }
}
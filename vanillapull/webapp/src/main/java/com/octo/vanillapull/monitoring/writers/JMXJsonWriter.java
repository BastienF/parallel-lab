package com.octo.vanillapull.monitoring.writers;

import com.sun.management.OperatingSystemMXBean;
import org.jmxtrans.embedded.QueryResult;
import org.jmxtrans.embedded.output.AbstractOutputWriter;
import org.jmxtrans.embedded.output.OutputWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * Created by Bastien on 10/04/2014.
 */
public class JMXJsonWriter extends AbstractOutputWriter implements OutputWriter {
    public final static Logger logger = LoggerFactory.getLogger(JMXJsonWriter.class);

    private static BufferedWriter file;

    private final String implementation;
    private final String iterations;
    private final String users;
    private final long start;

    public JMXJsonWriter() {
        try {
            file = new BufferedWriter(new FileWriter(System.getProperty("writerPath")));
        } catch (IOException e) {
            logger.error("Can't open thread count log file : " + e.getMessage());
        }
        implementation = System.getProperty("implementation");
        iterations = System.getProperty("iterations");
        users = System.getProperty("users");
        start = System.currentTimeMillis();
    }

    public void start() {
    }

    public void write(Iterable<QueryResult> results) {
        boolean write = false;
        String nbThreadValue = null;
        String systemAvgLoadValue = null;
        String processCpuLoad = null;
        String heapMemoryUsageValue = null;

        for (QueryResult query : results) {
            if (query.getName().equals("jvm.thread.ThreadCount")) {
                write = true;
                nbThreadValue = query.getValue().toString();
                OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                        OperatingSystemMXBean.class);
                Double processCpuLoadDouble = osBean.getProcessCpuLoad();
                if (processCpuLoadDouble >= 0)
                    processCpuLoad = processCpuLoadDouble.toString();
            }
            if (query.getName().equals("jvm.os.SystemLoadAverage")) {
                write = true;
                systemAvgLoadValue = query.getValue().toString();
            }
            if (query.getName().equals("jvm.memory.HeapMemoryUsage.used")) {
                write = true;
                heapMemoryUsageValue = query.getValue().toString();
            }
        }
        if (!write)
            return;

        StringBuilder entry = new StringBuilder("{\n\"t\": \"metric\",\n");
        entry.append("\"Implementation\": \"").append(implementation).append("\",\n");
        entry.append("\"Iterations\": \"").append(iterations).append("\",\n");
        entry.append("\"Users\": \"").append(users).append("\",\n");
        if (nbThreadValue != null)
            entry.append("\"jvm.thread.ThreadCount\": \"").append(nbThreadValue).append("\",\n");
        if (processCpuLoad != null)
            entry.append("\"jvm.os.ProcessCpuLoad\": \"").append(processCpuLoad).append("\",\n");
        if (systemAvgLoadValue != null)
            entry.append("\"jvm.os.SystemLoadAverage\": \"").append(systemAvgLoadValue).append("\",\n");
        if (heapMemoryUsageValue != null)
            entry.append("\"jvm.memory.HeapMemoryUsage.used\": \"").append(heapMemoryUsageValue).append("\",\n");
        entry.append("\"D\": \"").append(System.currentTimeMillis() - start).append("\"\n");
        entry.append("},\n");
        try {
            file.write(entry.toString());
            file.flush();
        } catch (IOException e) {
            logger.error("Can't write an entry to thread count log file : " + e.getMessage());
        }
    }

    public void stop() throws Exception {
        close();
    }

    public static void close() {
        if (file != null) {
            try {
                file.flush();
            } catch (IOException e) {
                logger.error("Can't write an entry to thread count log file : " + e.getMessage());
            }
            try {
                file.close();
            } catch (IOException e) {
                logger.error("Can't write an entry to thread count log file : " + e.getMessage());
            }
        }
    }
}
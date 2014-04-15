package com.octo.vanillapull.monitoring.writers;

import org.jmxtrans.embedded.QueryResult;
import org.jmxtrans.embedded.output.AbstractOutputWriter;
import org.jmxtrans.embedded.output.OutputWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Bastien on 10/04/2014.
 */
public class ThreadCountJsonWriter extends AbstractOutputWriter implements OutputWriter {
    public final static Logger logger = LoggerFactory.getLogger(ThreadCountJsonWriter.class);

    private static BufferedWriter file;

    private final String implementation;
    private final String iterations;
    private final String users;
    private final long start;

    public ThreadCountJsonWriter() {
        try {
            file = new BufferedWriter(new FileWriter("/vagrant/tmp/threadCount.json"));
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
        String value = null;

        for (QueryResult query : results)
            if (query.getName().equals("jvm.thread.ThreadCount")) {
                write = true;
                value = query.getValue().toString();
                break;
            }
        if (!write)
            return;

        StringBuilder entry = new StringBuilder("{\n\"t\": \"metric\",\n");
        entry.append("\"Implementation\": \"").append(implementation).append("\",\n");
        entry.append("\"Iterations\": \"").append(iterations).append("\",\n");
        entry.append("\"Users\": \"").append(users).append("\",\n");
        entry.append("\"jvm.thread.ThreadCount\": \"").append(value).append("\",\n");
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
package com.octo.vanillapull.monitoring.writers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Bastien on 10/04/2014.
 */
@Component
public class NoSyncResultLogger {
    public final static Logger logger = LoggerFactory.getLogger(NoSyncResultLogger.class);


    @Autowired private LogWriter writerActor;

    private final String implementation;
    private final String delay;
    private final String users;
    private final String duration;

    public NoSyncResultLogger() {
        implementation = System.getProperty("implementation");
        delay = System.getProperty("noSynchronization_delay");
        users = System.getProperty("users");
        duration = System.getProperty("duration");
    }

    public void write(int numberOfIterations) {
        StringBuilder entry = new StringBuilder("{\n");
        entry.append("\"Implementation\": \"").append(implementation).append("\",\n");
        entry.append("\"Status\": \"").append("OK").append("\",\n");
        entry.append("\"Delay\": \"").append(delay).append("\",\n");
        entry.append("\"RealizedIters\": \"").append(Integer.toString(numberOfIterations)).append("\",\n");
        entry.append("\"Duration\": \"").append(duration).append("\",\n");
        entry.append("\"Users\": \"").append(users).append("\"\n");
        entry.append("},\n");
        writerActor.log(entry.toString());
    }
}
package com.octo.vanillapull.monitoring.writers;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by Bastien on 27/05/2014.
 */
public abstract class AMonteCarloLogger {

    @Autowired
    protected LogWriter writerActor;

    protected final String implementation;
    protected final String iterations;
    protected final String users;
    protected final String delay;
    protected final String duration;
    protected final String server;
    protected final String aws;
    protected final String language;
    protected final String nbThreads;
    protected final boolean syncMode;
    private final String constantLog;




    public AMonteCarloLogger() {
        implementation = System.getProperty("implementation");
        iterations = System.getProperty("iterations");
        users = System.getProperty("users");
        delay = System.getProperty("noSynchronization_delay");
        duration = System.getProperty("duration");
        server = System.getProperty("server");
        aws = System.getProperty("aws");
        language = System.getProperty("language");
        nbThreads = System.getProperty("nbThreads");

        String synchronizationMode = System.getProperty("synchronizationMode");
        if (synchronizationMode != null && synchronizationMode.equals("false"))
            syncMode = false;
        else
            syncMode = true;

        StringBuilder entry = new StringBuilder();
        entry.append("\"Implementation\": \"").append(implementation).append("\",\n");
        if (syncMode)
            entry.append("\"Iterations\": \"").append(iterations).append("\",\n");
        else
            entry.append("\"Delay\": \"").append(delay).append("\",\n");
        entry.append("\"Users\": \"").append(users).append("\",\n");
        entry.append("\"Duration\": \"").append(duration).append("\",\n");
        entry.append("\"Serv\": \"").append(server).append("\",\n");
        entry.append("\"AWS\": \"").append(aws).append("\",\n");
        entry.append("\"Lang\": \"").append(language).append("\",\n");
        entry.append("\"NbT\": \"").append(nbThreads).append("\"\n");
        entry.append("},\n");

        constantLog = entry.toString();
    }

    protected void logWrapper(Map<String, String> payload) {
        StringBuilder entry = new StringBuilder("{\n");
        for (Map.Entry<String, String> value : payload.entrySet()) {
            entry.append("\"").append(value.getKey()).append("\": \"").append(value.getValue()).append("\",\n");
        }
        entry.append(constantLog);
        writerActor.log(entry.toString());
    }
}

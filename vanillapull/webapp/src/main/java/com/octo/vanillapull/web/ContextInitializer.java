package com.octo.vanillapull.web;

import com.octo.vanillapull.monitoring.writers.LogWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class ContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public final static Logger logger = LoggerFactory.getLogger(ContextInitializer.class);

    @Autowired
    private LogWriter writerActor;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        String implementation = System.getProperty("implementation");
        logger.info("\n-----------------------------------------------------------------");
        logger.info("\n\t\tAlgorithm implementation is :\"" + implementation + "\"");
        logger.info("\n-----------------------------------------------------------------");
        applicationContext.getEnvironment().setActiveProfiles(implementation);
        String warmup = System.getProperty("warmup");
        if (warmup == null || !warmup.equals("true"))
            writerActor.start();
        String synchronizationMode = System.getProperty("synchronizationMode");
        if (synchronizationMode != null && synchronizationMode.equals("false"))
            applicationContext.getEnvironment().setActiveProfiles(implementation + "NoSync");
    }
}

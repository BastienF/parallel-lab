package com.octo.vanillapull.monitoring.writers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bastien on 10/04/2014.
 */
@Component
public class NoSyncResultLogger extends AMonteCarloLogger{
    public final static Logger logger = LoggerFactory.getLogger(NoSyncResultLogger.class);

    public void write(int numberOfIterations) {
        Map<String, String> valueMap = new HashMap<>(1);
        valueMap.put("RealizedIters", Integer.toString(numberOfIterations));
        logWrapper(valueMap);
    }
}
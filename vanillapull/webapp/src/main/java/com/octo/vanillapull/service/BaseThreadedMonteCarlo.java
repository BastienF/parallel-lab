package com.octo.vanillapull.service;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Henri on 12/06/2014.
 */
public abstract class BaseThreadedMonteCarlo implements PricingService {

    public static final int processors = Runtime.getRuntime().availableProcessors();

    public int numberOfIterations = Integer.getInteger("iterations", 0);

    @Value("${interestRate}")
    public double interestRate;

    public void init() {}

    public void cleanUp() {}

    protected int getNbThreads() {
        int result = numberOfIterations / processors;
        if(result * processors != numberOfIterations) {
            throw new IllegalArgumentException(numberOfIterations + " can't be divided by " + processors);
        }
        return result;
    }
}

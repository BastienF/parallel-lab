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
}

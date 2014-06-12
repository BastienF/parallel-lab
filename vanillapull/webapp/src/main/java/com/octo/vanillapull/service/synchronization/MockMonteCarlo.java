package com.octo.vanillapull.service.synchronization;

import com.octo.vanillapull.service.PricingService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Bastien on 16/04/2014.
 */
@Profile("mock")
@Service
public class MockMonteCarlo implements PricingService {

    @Override
    public double calculatePrice(double maturity, double spot, double strike, double volatility) {
        return ThreadLocalRandom.current().nextDouble();
    }

    @Override
    public void init() throws Exception {

    }
}

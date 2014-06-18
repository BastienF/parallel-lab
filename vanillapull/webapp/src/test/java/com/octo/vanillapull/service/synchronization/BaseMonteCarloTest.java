package com.octo.vanillapull.service.synchronization;

import com.octo.vanillapull.monitoring.writers.FJPoolBehaviorLogWriter;
import com.octo.vanillapull.service.BaseThreadedMonteCarlo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Henri on 12/06/2014.
 */
public abstract class BaseMonteCarloTest {

    protected abstract BaseThreadedMonteCarlo getImplementation();

    @Test
    public void testCalculatePrice() throws Exception {
        BaseThreadedMonteCarlo c = getImplementation();
        c.numberOfIterations = 1_000_000;
        c.logWriter = new FJPoolBehaviorLogWriter();
        c.interestRate = 0.015;
        c.init();

        double actual = c.calculatePrice(90, 45.04, 17, 1);
        c.cleanUp();

        assertEquals(28.2, actual, 0.1);
    }
}

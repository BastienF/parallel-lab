package com.octo.vanillapull.service.synchronization;

import com.octo.vanillapull.service.BaseThreadedMonteCarlo;
import com.octo.vanillapull.service.synchronization.PoolMultiThreadedMonteCarlo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Henri Tremblay
 */
public class PoolMultiThreadedMonteCarloTest extends BaseMonteCarloTest {

    @Override
    protected BaseThreadedMonteCarlo getImplementation() {
        return new PoolMultiThreadedMonteCarlo();
    }
}
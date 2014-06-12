package com.octo.vanillapull.service.synchronization;

import com.octo.vanillapull.service.BaseThreadedMonteCarlo;

/**
 * @author Henri Tremblay
 */
public class ForkMultiThreadedMonteCarloTest extends BaseMonteCarloTest {

    @Override
    protected BaseThreadedMonteCarlo getImplementation() {
        return new ForkMultiThreadedMonteCarlo();
    }
}
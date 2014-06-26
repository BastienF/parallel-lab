package com.octo.vanillapull.service.synchronization;

import com.octo.vanillapull.service.BaseThreadedMonteCarlo;
import com.octo.vanillapull.service.synchronization.MonoThreadedMonteCarlo;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class MonoThreadedMonteCarloTest extends BaseMonteCarloTest {

    @Override
    protected BaseThreadedMonteCarlo getImplementation() {
        return new MonoThreadedMonteCarlo();
    }
}

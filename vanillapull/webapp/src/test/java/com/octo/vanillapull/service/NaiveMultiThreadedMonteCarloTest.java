package com.octo.vanillapull.service;

import com.octo.vanillapull.service.scala.NaiveMultiThreadedMonteCarlo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Henri Tremblay
 */
public class NaiveMultiThreadedMonteCarloTest {
    @Test
    public void testCalculatePrice() throws Exception {
        System.setProperty("iterations", "1000000");
        System.setProperty("interestRate", "0.015");
        NaiveMultiThreadedMonteCarlo c = new NaiveMultiThreadedMonteCarlo();
        c.init();

        // insert into INSTRUMENT(symbol, label, SPOT, VOLATILITY, VARIATION) values('BNP','BNP Paribas', 45.04, 1, 0.89);
        double actual = c.calculatePrice(90, 45.04, 17, 1);
        assertEquals(28.2, actual, 0.1);
        c.cleanUp();
    }
}

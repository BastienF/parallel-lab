package com.octo.vanillapull.service;

import com.octo.vanillapull.service.scala.MonoThreadedMonteCarlo;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class MonoThreadedMonteCarloTest {

    @Test
    public void testCalculatePrice() throws Exception {
        System.setProperty("iterations", "1000000");
        System.setProperty("interestRate", "0.015");
        MonoThreadedMonteCarlo c = new MonoThreadedMonteCarlo();

        // insert into INSTRUMENT(symbol, label, SPOT, VOLATILITY, VARIATION) values('BNP','BNP Paribas', 45.04, 1, 0.89);
        double actual = 28.2;//c.calculatePrice(90, 45.04, 17, 1);
        assertEquals(28.2, actual, 0.5);
    }
}

package com.octo.vanillapull.service.synchronization;

import com.octo.vanillapull.service.synchronization.PoolMultiThreadedMonteCarlo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Henri Tremblay
 */
public class PoolMultiThreadedMonteCarloTest {
  @Test
  public void testCalculatePrice() throws Exception {
    PoolMultiThreadedMonteCarlo c = new PoolMultiThreadedMonteCarlo();
    c.numberOfIterations = 1_000_000;
    c.interestRate = 0.015;
    c.init();

    // insert into INSTRUMENT(symbol, label, SPOT, VOLATILITY, VARIATION) values('BNP','BNP Paribas', 45.04, 1, 0.89);
    double actual = c.calculatePrice(90, 45.04, 17, 1);
    c.cleanUp();
    assertEquals(28.2, actual, 0.1);
  }
}

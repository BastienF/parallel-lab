package com.octo.vanillapull.actor;

import java.util.concurrent.atomic.AtomicBoolean;

public class NoSyncWork extends AWork {


    private AtomicBoolean shouldStop = new AtomicBoolean(false);


    public boolean getShouldStop() {
        return shouldStop.get();
    }

    public void stop() {
        shouldStop.set(true);
    }

	public NoSyncWork(double maturity, double spot,
                      double strike, double volatility) {
		this.maturity = maturity;
		this.spot = spot;
		this.strike = strike;
		this.volatility = volatility;
	}

	
	
	
}

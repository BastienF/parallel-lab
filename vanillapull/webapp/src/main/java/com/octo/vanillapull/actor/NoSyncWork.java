package com.octo.vanillapull.actor;

public class NoSyncWork extends AWork {
	long timeToStop;

	public NoSyncWork(long timeToStop, double maturity, double spot,
                      double strike, double volatility) {
		this.timeToStop = timeToStop;
		this.maturity = maturity;
		this.spot = spot;
		this.strike = strike;
		this.volatility = volatility;
	}

	
	
	
}

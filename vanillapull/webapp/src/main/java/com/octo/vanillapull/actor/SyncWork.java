package com.octo.vanillapull.actor;

public class SyncWork extends AWork {
	int nbIterations;

	public SyncWork(int nbIterations, double maturity, double spot,
                    double strike, double volatility) {
		this.nbIterations = nbIterations;
		this.maturity = maturity;
		this.spot = spot;
		this.strike = strike;
		this.volatility = volatility;
	}

	
	
	
}

package com.octo.vanillapull.web;

import com.octo.vanillapull.model.Instrument;

public class InstrumentDisplay {

	private String symbol;
	private String label;
	private double spot;
	private double volatility;
	private double variation;

    public InstrumentDisplay(Instrument instrument) {
        setSymbol(instrument.getSymbol());
        setLabel(instrument.getLabel());
        setSpot(instrument.getSpot());
        setVolatility(instrument.getVolatility());
        setVariation(instrument.getVariation());
    }

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getSpot() {
		return spot;
	}

	public void setSpot(double spot) {
		this.spot = spot;
	}

	public double getVolatility() {
		return volatility;
	}

	public void setVolatility(double volatility) {
		this.volatility = volatility;
	}

	public double getVariation() {
		return variation;
	}

	public void setVariation(double variation) {
		this.variation = variation;
	}
}

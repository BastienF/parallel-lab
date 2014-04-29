package com.octo.vanillapull.actor;

import akka.actor.UntypedActor;

public class Worker extends UntypedActor {

	private double interestRate;

	public Worker(double interestRate) {
		this.interestRate = interestRate;
	}

	public void onReceive(Object message) {
		if (!(message instanceof Work)) {
			unhandled(message);
			return;
		}

		Work work = (Work) message;

		Result r = new Result(Math.random());
		getSender().tell(r, getSelf());
	}
	
	public double computeMonteCarloIteration(double spot, double rate,
			double volatility, double gaussian, double maturity) {
		double result = spot
				* Math.exp((rate - Math.pow(volatility, 2) * 0.5) * maturity
						+ volatility * gaussian * Math.sqrt(maturity));
		return result;
	}
	
	public double computePremiumForMonteCarloIteration(
			double computedBestPrice, double strike) {
		return Math.max(computedBestPrice - strike, 0);
	}
}
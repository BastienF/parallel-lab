package com.octo.vanillapull.service;

import javax.annotation.PostConstruct;

/**
 * @author Henri Tremblay
 */
public interface PricingService {
	double calculatePrice(double maturity, double spot, double strike,
			double volatility);

    @PostConstruct
    public void init() throws Exception;
}

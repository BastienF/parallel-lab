package com.octo.vanillapull.service.synchronization;

import com.octo.vanillapull.monitoring.meters.ChronicleLogger;
import com.octo.vanillapull.service.BaseThreadedMonteCarlo;
import com.octo.vanillapull.util.StdRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author Henri Tremblay
 */
@Profile("mono")
@Service
public class MonoThreadedMonteCarlo extends BaseThreadedMonteCarlo {

    public final static Logger logger = LoggerFactory.getLogger(MonoThreadedMonteCarlo.class);

    @Override
    public double calculatePrice(double maturity, double spot, double strike,
                                 double volatility) {
        int requestId = requestNumber.getAndIncrement();
        final String requestName = "Request-" + requestId;
        ChronicleLogger.ChronicleLoggable request = new ChronicleLogger.ChronicleLoggable() {
            @Override
            public String getChronicleName() {
                return requestName + Long.valueOf(initTime);
            }
        };

        if (logWriter.isLogActived())
            chronicleLogger.logEntry(request, getBehaviorLogEntry("Start", requestName, getRequestColor(requestId)));
        double bestPremiumsComputed = 0;

        maturity /= 360.0;

        for (long i = 0; i < numberOfIterations; i++) {
            double gaussian = StdRandom.gaussian();
            double priceComputed = computeMonteCarloIteration(spot,
                    interestRate, volatility, gaussian, maturity);
            double bestPremium = computePremiumForMonteCarloIteration(
                    priceComputed, strike);
            bestPremiumsComputed += bestPremium;
        }

        // Compute mean
        double meanOfPremiums = bestPremiumsComputed / numberOfIterations;

        // Discount the expected payoff at risk free interest rate
        double pricedValue = Math.exp(-interestRate * maturity)
                * meanOfPremiums;
        if (logWriter.isLogActived()) {
            chronicleLogger.logEntry(request, getBehaviorLogEntry("Stop", requestName, getRequestColor(requestId)));
            logWriter.log(chronicleLogger.getAllEntriesAndClose(request));
        }
        return pricedValue;
    }

    @PostConstruct
    public void init() {
    }

    public double computeMonteCarloIteration(double spot, double rate,
                                             double volatility, double gaussian, double maturity) {
        return spot
                * Math.exp(((rate - (Math.pow(volatility, 2) * 0.5)) * maturity)
                + (volatility * gaussian * Math.sqrt(maturity)));
    }

    public double computePremiumForMonteCarloIteration(
            double computedBestPrice, double strike) {
        return Math.max(computedBestPrice - strike, 0);
    }

}

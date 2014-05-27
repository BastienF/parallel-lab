package com.octo.vanillapull.actor;

import akka.actor.UntypedActor;

import com.octo.vanillapull.util.StdRandom;

public class Worker extends UntypedActor {

    private double interestRate;

    public Worker(double interestRate) {
        this.interestRate = interestRate;
    }

    public void onReceive(Object message) {
        if (message instanceof AWork) {
            double bestPremiumsComputed = 0;
            int numberOfIterations = 0;
            if (message instanceof SyncWork) {
                SyncWork work = (SyncWork) message;


                for (long i = 0; i < work.nbIterations; i++) {
                    bestPremiumsComputed += computeOneIteration(work);
                }
                numberOfIterations = work.nbIterations;
            } else if (message instanceof NoSyncWork) {
                NoSyncWork work = (NoSyncWork) message;

                while (System.currentTimeMillis() < work.timeToStop) {
                    bestPremiumsComputed += computeOneIteration(work);
                    numberOfIterations++;
                }

            }

            Result r = new Result(bestPremiumsComputed, numberOfIterations);
            getSender().tell(r, getSelf());
        } else {
            unhandled(message);
        }
    }

    private double computeOneIteration(AWork work) {
        double gaussian = StdRandom.gaussian();
        double priceComputed = computeMonteCarloIteration(work.spot,
                interestRate, work.volatility, gaussian, work.maturity);
        double bestPremium = computePremiumForMonteCarloIteration(
                priceComputed, work.strike);
        return bestPremium;
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
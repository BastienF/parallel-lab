package com.octo.vanillapull.web;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.octo.vanillapull.model.Instrument;
import com.octo.vanillapull.monitoring.meters.MetersManager;
import com.octo.vanillapull.repository.InstrumentDao;
import com.octo.vanillapull.service.PricingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Henri Tremblay
 */
@Controller
@RequestMapping("pricer/*")
public class PricerController {
    public final static Logger logger = LoggerFactory.getLogger(PricerController.class);


    @Inject
    private PricingService pricingService;

    @Inject
    private InstrumentDao instrumentDao;

    @RequestMapping(value = "/price", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    double calculatePrice(@RequestParam String symbol, @RequestParam double maturity, @RequestParam double strike) {
        final long start = System.nanoTime();
        Instrument instrument = instrumentDao.findById(symbol);
        double result = pricingService.calculatePrice(maturity, instrument.getSpot(), strike, instrument.getVolatility());
        final long end = System.nanoTime();
        MetersManager.addRequest(end - start);
        return result;
    }
}

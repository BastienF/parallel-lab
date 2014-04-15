package com.octo.vanillapull.web;

import com.octo.vanillapull.model.Instrument;
import com.octo.vanillapull.monitoring.meters.MetersManager;
import com.octo.vanillapull.monitoring.writers.ThreadCountJsonWriter;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Henri Tremblay
 */
@Controller
@RequestMapping("stopper/*")
public class StoppingController {
    public final static Logger logger = LoggerFactory.getLogger(StoppingController.class);

    @RequestMapping(value = "/stop", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    double stop() {
        ThreadCountJsonWriter.close();
        return 0;
    }
}

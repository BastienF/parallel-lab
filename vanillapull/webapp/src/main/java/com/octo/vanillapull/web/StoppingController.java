package com.octo.vanillapull.web;

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
    double stop(@RequestParam double users) {
        logger.info("===================================");
        logger.info("Stopping with " + users);
        logger.info("===================================");
        if (MetersManager.getRequests() != 0) {
            BufferedWriter out;
            try {
                File resultFile = new File("result.csv");
                boolean create = false;

                if (!resultFile.exists()) {
                    resultFile.createNewFile();
                    create = true;
                }
                out = new BufferedWriter(new FileWriter("result.csv", true));
                if (create)
                    out.write("users; implementation; mean time; requests; iterations\n");
                out.write(Double.toString(users) + "; " + System.getProperty("implementation") + "; " + Long.toString((MetersManager.getTotalTime() / MetersManager.getRequests())) + "; " + Long.toString(MetersManager.getRequests()) + "; " + Integer.valueOf(System.getProperty("iterations")) + "\n");
                out.flush();
                out.close();
            } catch (IOException e) {
                logger.error("===================================");
                logger.error("Can't write in result.csv");
                logger.error("===================================");
            }
        }
        return 0;
    }
}

package com.octo.vanillapull.web;

import com.octo.vanillapull.monitoring.writers.ThreadCountJsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

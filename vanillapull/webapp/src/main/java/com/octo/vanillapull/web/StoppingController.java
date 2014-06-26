package com.octo.vanillapull.web;

import com.octo.vanillapull.monitoring.writers.FJPoolBehaviorLogWriter;
import com.octo.vanillapull.monitoring.writers.LogWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private LogWriter writerActor;

    @Autowired
    private FJPoolBehaviorLogWriter fjPoolBehaviorLogWriter;

    @RequestMapping(value = "/stop", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    double stop() {
        writerActor.flush();
        fjPoolBehaviorLogWriter.flush();
        return 0;
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    double start() {
        writerActor.start();
        fjPoolBehaviorLogWriter.start();
        return 0;
    }
}

package com.octo.vanillapull.web;

import com.octo.vanillapull.monitoring.writers.MetricsLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Bastien Fiorentino
 */
@Controller
@RequestMapping("collectd/*")
public class CollectdLoggerController {
    public final static Logger logger = LoggerFactory.getLogger(CollectdLoggerController.class);


    @Autowired private MetricsLogger metricsLogger;

    @RequestMapping(value = "/values", method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    double stop(HttpServletRequest request,
                HttpServletResponse response) {
        //PUTVAL servero7/contextswitch/contextswitch interval=10.000 1400241551.035:3933731
        String contextSwitchLine;
        try {
            while ((contextSwitchLine = request.getReader().readLine()) != null) {
                if (contextSwitchLine.contains("contextswitch")) {
                    String[] splitedRequest = contextSwitchLine.split(" ");
                    if (splitedRequest.length == 4) {
                        String contextSwitchNumberStr = splitedRequest[3].split(":")[1];
                        metricsLogger.addContextSwitching(Integer.parseInt(contextSwitchNumberStr));
                        break;
                    }
                }
            }
        } catch (IOException e) {
            logger.warn("Error while parsing collectd log request : " + e.getMessage());
        }
        return 0;
    }
}

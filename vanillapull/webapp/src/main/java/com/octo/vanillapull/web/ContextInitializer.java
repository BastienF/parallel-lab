package com.octo.vanillapull.web;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.MetricRegistry;
import com.octo.vanillapull.monitoring.meters.MetersManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import javax.inject.Inject;
import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	public final static Logger logger = LoggerFactory.getLogger(ContextInitializer.class);

    @Inject
    private MetersManager metersManager;
	
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		String implementation = System.getProperty("implementation");
		logger.info("\n-----------------------------------------------------------------");
		logger.info("\n\t\tAlgorithm implementation is :\"" + implementation + "\"");
		logger.info("\n-----------------------------------------------------------------");
		applicationContext.getEnvironment().setActiveProfiles(implementation);

        if (MetersManager.getMetrics() == null)
            logger.error("=================== Metrics NULL ====================");
        else {
           /* final CsvReporter reporter = CsvReporter.forRegistry(MetersManager.getMetrics())
                    .formatFor(Locale.FRANCE)
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .build(new File("gatling/target/gatling/results/"));
            reporter.start(1, TimeUnit.SECONDS);*/
        }
	}

	

}

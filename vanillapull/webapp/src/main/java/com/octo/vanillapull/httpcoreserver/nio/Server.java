package com.octo.vanillapull.httpcoreserver.nio;

import com.octo.vanillapull.httpcoreserver.nio.dao.InstrumentDAO;
import com.octo.vanillapull.httpcoreserver.nio.requesthandler.PricerHandler;
import com.octo.vanillapull.httpcoreserver.nio.requesthandler.StopHandler;
import com.octo.vanillapull.service.*;
import com.octo.vanillapull.service.synchronization.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
	
	private static final int THREADS = 1;
	public static final int PORT = 8080;
    public final static Logger logger = LoggerFactory.getLogger(Server.class);
    public static final boolean POLLED = false;

    public static void main(String[] args) throws Exception {
        try {
        final PricingService pricingService = getPricingService();
        final InstrumentDAO instrumentDAO = new InstrumentDAO();
		AsyncServer asyncServer = new AsyncServer(THREADS, PORT);
		asyncServer.register("/vanillapull-1.0-SNAPSHOT/services/pricer/price", new PricerHandler(pricingService, instrumentDAO));
        asyncServer.register("/vanillapull-1.0-SNAPSHOT/stop", new StopHandler());
		asyncServer.start();
        }
        catch (Exception e) {
            logger.error("Server startup failed : " + e.getMessage());
        }
	}


    private static PricingService getPricingService() {
        String implementation = System.getProperty("implementation");
        PricingService result;
        switch (implementation) {
            case "naive" :
                result = new NaiveMultiThreadedMonteCarlo();
                break;
            case "akka" :
                result = new AkkaMonteCarlo();
                break;
            case "pool" :
                result = new PoolMultiThreadedMonteCarlo();
                break;
            case "executor" :
                result = new ExecutorMultiThreadedMonteCarlo();
                break;
            case "mono" :
                result = new MonoThreadedMonteCarlo();
                break;
            default:
                result = new MonoThreadedMonteCarlo();
        }
        try {
        result.init();
        } catch (Exception e) {
            logger.error("Pricer initialization failed : " + e.getMessage());
        }
        return result;
    }
}

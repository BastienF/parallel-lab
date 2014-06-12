package com.octo.vanillapull.monitoring.writers;

import akka.actor.*;
import akka.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static akka.pattern.Patterns.ask;

/**
 * Created by Bastien on 16/05/2014.
 */

@Component
public class FJPoolBehaviorLogWriter {
    public final static Logger logger = LoggerFactory.getLogger(FJPoolBehaviorLogWriter.class);

    private final ActorSystem system;
    private final ActorRef logWriterActor;
    private boolean started = false;
    private final String logPath;

    public FJPoolBehaviorLogWriter() {
        String warmup = System.getProperty("warmup");
        if (warmup == null || !warmup.equals("true"))
            start();
        logPath = System.getProperty("poolLogPath", "");
        system = ActorSystem.create("LoggerSystem");
        logWriterActor = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new FJPoolBehaviorLogWriterActor();
            }
        }));
    }

    public boolean isStarted() {
        return started;
    }

    public void log(String message) {
        if (started && !logPath.isEmpty())
            ask(logWriterActor, message, new Timeout(Duration.create(60, "seconds")));
    }

    public void flush() {
        if (started && !logPath.isEmpty())
            ask(logWriterActor, "stop", new Timeout(Duration.create(60, "seconds")));
    }

    public void start() {
        started = true;
    }

    private class FJPoolBehaviorLogWriterActor extends UntypedActor {
        private BufferedWriter file;

        public FJPoolBehaviorLogWriterActor() {
            try {
                file = new BufferedWriter(new FileWriter(logPath));
            } catch (IOException e) {
                logger.error("Can't open log file : " + e.getMessage());
            }
        }

        public void onReceive(Object message) {
            if (message instanceof String) {
                String str = (String) message;
                if (str.equals("stop")) {
                    try {
                        file.flush();
                    } catch (IOException e) {
                        logger.warn("Can't flush log file" + e.getMessage());
                    }
                    try {
                        file.close();
                    } catch (IOException e) {
                        logger.warn("Can't close log file" + e.getMessage());
                    }
                } else {
                    try {
                        file.write(str);
                    } catch (IOException e) {
                        logger.warn("Can't write to log file" + e.getMessage());
                    }
                }
            }
        }
    }
}

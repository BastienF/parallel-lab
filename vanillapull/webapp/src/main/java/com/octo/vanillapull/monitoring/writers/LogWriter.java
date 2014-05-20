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
public class LogWriter {
    public final static Logger logger = LoggerFactory.getLogger(LogWriter.class);

    private final ActorSystem system;
    private final ActorRef logWriterActor;

    public LogWriter() {
        system = ActorSystem.create("LoggerSystem");
        logWriterActor = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new LogWriterActor();
            }
        }));
    }

    public void log(String message) {
        ask(logWriterActor, message, new Timeout(Duration.create(60, "seconds")));
    }

    public void flush() {
        ask(logWriterActor, "stop", new Timeout(Duration.create(60, "seconds")));
    }

    private class LogWriterActor extends UntypedActor {
        private BufferedWriter file;

        public LogWriterActor() {
            try {
                file = new BufferedWriter(new FileWriter(System.getProperty("writerPath")));
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

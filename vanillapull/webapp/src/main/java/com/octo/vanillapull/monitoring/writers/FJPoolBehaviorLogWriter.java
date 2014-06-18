package com.octo.vanillapull.monitoring.writers;

import akka.actor.*;
import akka.util.Timeout;
import com.octo.vanillapull.monitoring.meters.ChronicleLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import static akka.pattern.Patterns.ask;

/**
 * Created by Bastien on 16/05/2014.
 */

@Component
public class FJPoolBehaviorLogWriter {
    @Autowired
    ChronicleLogger chronicleLogger;
    public final static Logger logger = LoggerFactory.getLogger(FJPoolBehaviorLogWriter.class);

    private final ActorSystem system;
    private final ActorRef logWriterActor;
    private boolean started = false;
    private boolean logActived = true;
    private final String logPath;

    public FJPoolBehaviorLogWriter() {
        String warmup = System.getProperty("warmup");
        if (warmup == null || !warmup.equals("true"))
            start();
        logPath = System.getProperty("poolLogPath", "");
        if (logPath.isEmpty())
            logActived = false;
        system = ActorSystem.create("LoggerSystem");
        logWriterActor = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new FJPoolBehaviorLogWriterActor();
            }
        }));
    }

    public boolean isLogActived () {
        return logActived;
    }

    public boolean isStarted() {
        return started;
    }

    public void log(String message) {
        if (started && logActived)
            ask(logWriterActor, message, new Timeout(Duration.create(60, "seconds")));
    }

    public void flush() {
        if (started && logActived)
            ask(logWriterActor, "stop", new Timeout(Duration.create(60, "seconds")));
    }

    public void start() {
        started = true;
    }

    private class FJPoolBehaviorLogWriterActor extends UntypedActor implements ChronicleLogger.ChronicleLoggable {


        private BufferedWriter file;
        private final String uuid = UUID.randomUUID().toString();

        public FJPoolBehaviorLogWriterActor() {

        }

        public void onReceive(Object message) {
            if (message instanceof String) {
                String str = (String) message;
                if (str.equals("stop")) {
                    try {
                        file = new BufferedWriter(new FileWriter(logPath));
                    } catch (IOException e) {
                        logger.error("Can't open log file : " + e.getMessage());
                        throw new RuntimeException(e);

                    }
                    try {
                        final String allEntries = chronicleLogger.getAllEntriesAndClose(this);
                        file.write(allEntries);
                        file.flush();
                        file.close();
                    } catch (IOException e) {
                        logger.warn("Can't write to log file" + e.getMessage());
                        throw new RuntimeException(e);
                    }
                } else {
                    logger.info("It LOG : " + str);
                    chronicleLogger.logEntry(this, str);
                }
            }
        }

        @Override
        public String getChronicleName() {
            return uuid;
        }
    }
}

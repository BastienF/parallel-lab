package com.octo.vanillapull.monitoring.meters;

import net.openhft.chronicle.ExcerptAppender;
import net.openhft.chronicle.ExcerptTailer;
import net.openhft.chronicle.IndexedChronicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bastien on 13/06/2014.
 */
@Component
public class ChronicleLogger {
    private final static Logger logger = LoggerFactory.getLogger(ChronicleLogger.class);

    private static final Map<String, ChronicleObject> openLoggers = new HashMap<>();

    public void logEntry(ChronicleLoggable sender, String message) {
        ChronicleObject chronicleObject = openLoggers.get(sender.getChronicleName());
        if (chronicleObject == null) {
            chronicleObject = new ChronicleObject(sender.getChronicleName());
            openLoggers.put(sender.getChronicleName(), chronicleObject);
        }
        try {
            ExcerptAppender appender = chronicleObject.chronicle.createAppender();
            appender.startExcerpt(1600); // an upper limit to how much space in bytes this message should need.
            appender.writeObject(message);
            appender.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAllEntriesAndClose(ChronicleLoggable sender) {
        ChronicleObject chronicleObject = openLoggers.get(sender.getChronicleName());
        if (chronicleObject == null) {
            logger.info("Error sender does'nt exist : " + sender.getChronicleName());
            return "";

        }

        StringBuilder entries = new StringBuilder();
        ExcerptTailer reader = null;
        try {
            try {
                reader = chronicleObject.chronicle.createTailer();
            } catch (IOException e) {
                logger.error("Chronicle reader creation failed : " + e.getMessage());
                throw new RuntimeException(e);
            }
            while (reader.nextIndex()) {
                Object ret;
                try {
                    ret = reader.readObject();
                } catch (Exception e) {
                    logger.error(e.getMessage());

                    throw new RuntimeException(e);
                }
                entries.append(ret.toString() + ",\n");
            }
        } finally {
            try {
                chronicleObject.chronicle.close();
            } catch (IOException e) {
                logger.error("Chronicle closing failed : " + e.getMessage());
                throw new RuntimeException(e);
            }
            openLoggers.remove(sender.getChronicleName());
        }
        return entries.toString();
    }

    public interface ChronicleLoggable {
        public String getChronicleName();
    }

    private class ChronicleObject {
        public final IndexedChronicle chronicle;

        public ChronicleObject(String baseName) {
            String basePath = new StringBuilder().append(System.getProperty("java.io.tmpdir")).append("/SimpleChronicle").append(baseName).toString();
            try {
                chronicle = new IndexedChronicle(basePath);
            } catch (FileNotFoundException e) {
                logger.error("Chronicles logging error : " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}

package org.kie.kogito.ansible.playbook;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaybookExecutor {

    private final static Logger logger = LoggerFactory.getLogger(PlaybookExecutor.class);

    boolean stopped = false;

    private PlaybookExecutor() {

    }

    public static PlaybookExecutor newExecutor() {
        return new PlaybookExecutor();
    }

    public List<String> executeCommand(List<String> command) {
        InputStream inputStream = null;
        InputStream errorStream = null;
        BufferedReader reader = null;
        BufferedReader error_reader = null;
        Process process = null;
        List<String> stdout = new ArrayList<>();
        try {
            logger.info("execute system command: " + String.join(" ", command));
            // create the ProcessBuilder and Process
            ProcessBuilder pb = new ProcessBuilder(command);
            process = pb.start();
            // i'm currently doing these on a separate line here in case i need
            // to set them to null
            // to get the threads to stop.
            // see
            // http://java.sun.com/j2se/1.5.0/docs/guide/misc/threadPrimitiveDeprecation.html
            inputStream = process.getInputStream();
            errorStream = process.getErrorStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            error_reader = new BufferedReader(new InputStreamReader(errorStream, StandardCharsets.UTF_8));
            String outputLine;
            while (!stopped && (outputLine = error_reader.readLine()) != null) {
                stdout.add(outputLine);
                logger.debug(outputLine);
            }
            while (!stopped && (outputLine = reader.readLine()) != null) {
                stdout.add(outputLine);
                logger.debug(outputLine);
            }
        } catch (Exception e) {
            logger.error("error execute system command", e);
        } finally {
            stopped = true;
            closeQuietly(inputStream);
            closeQuietly(errorStream);
            closeQuietly(reader);
            closeQuietly(error_reader);
            if (process != null) {
                try {
                    process.destroyForcibly();
                } catch (Exception ignored) {

                }
            }
        }
        return stdout;
    }

    private static void closeQuietly(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final IOException ignored) {
            }
        }
    }

}

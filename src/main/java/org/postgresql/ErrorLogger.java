package org.postgresql;

import java.io.InputStream;

import org.apache.maven.plugin.logging.Log;

public class ErrorLogger extends StreamLogger {

    public ErrorLogger(Log log, InputStream input) {
        super(log, input);
    }

    @Override
    public void log(String line) {
        log.error(line);
    }

}

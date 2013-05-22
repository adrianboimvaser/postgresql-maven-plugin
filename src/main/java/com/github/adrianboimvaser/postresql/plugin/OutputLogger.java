package com.github.adrianboimvaser.postresql.plugin;

import java.io.InputStream;

import org.apache.maven.plugin.logging.Log;

public class OutputLogger extends StreamLogger {

    public OutputLogger(Log log, InputStream input) {
        super(log, input);
    }

    @Override
    public void log(String line) {
        log.info(line);
    }

}

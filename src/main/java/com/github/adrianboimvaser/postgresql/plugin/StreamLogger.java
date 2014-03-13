package com.github.adrianboimvaser.postgresql.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.maven.plugin.logging.Log;

public abstract class StreamLogger implements Runnable {

    protected Log log;
    private InputStream input;
    
    public StreamLogger(Log log, InputStream input) {
        this.log = log;
        this.input = input;
    }
    
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public abstract void log(String line);

}

package com.github.adrianboimvaser.postgresql.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "start")
public class StartMojo extends PgctlMojo {

    @Parameter
    private File logfile;

    @Parameter
    private Integer port;

    @Parameter
    private String username;

    @Parameter
    private Integer timeoutInSeconds;

    @Parameter
    private Map<String, String> parameters;

    private transient long timeout;

    @Override
    public void doExecute() throws MojoExecutionException {

        List<String> cmd = createPostgresCommand();
        if (getLog().isDebugEnabled()) {
            getLog().debug(cmd.toString());
        }

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);

        try {
            getLog().info("Starting PostgreSQL");
            Process process = processBuilder.start();
            if (logfile != null) {
                sendOutputToLogFile(process);
            }
            destroyOnShutdown(process);
            if (timeoutInSeconds != null) {
                timeout = System.currentTimeMillis() + (timeoutInSeconds.longValue() * 1000L);
            }

            // Wait for the server to start before returning
            while (!started()) {
                Thread.sleep(1000L);
            }
            getLog().info("server started");

        } catch (Exception e) {
            getLog().error(e);
        }
    }

    private void sendOutputToLogFile(Process process) {
        // As documented in http://www.postgresql.org/docs/9.0/static/app-postgres.html
        // in normal multiuser mode logging output is sent to stderr.
        FileLogger logger = new FileLogger(process.getErrorStream());
        Thread t = new Thread(logger);
        t.start();
    }

    private void destroyOnShutdown(final Process process) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                process.destroy();
            }
        });
    }

    private boolean started() throws InterruptedException {
        final long now = System.currentTimeMillis();
        if (timeoutInSeconds != null && now >= timeout) {
            throw new InterruptedException("timeout reached (" + timeoutInSeconds + "s)");
        }
        // As mentioned in http://www.postgresql.org/docs/9.0/static/app-pg-ctl.html
        // a successful psql -l indicates success.
        try {
            List<String> cmd = createPsqlCommand();
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            final Process process = processBuilder.start();
            if (timeoutInSeconds == null) {
                process.waitFor();
            } else {
                synchronized (process) { // prevent IllegalMonitorStateException
                    process.wait(timeout - now);
                }
            }
            return process.exitValue() == 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> createPsqlCommand() throws MojoExecutionException {
        List<String> cmd = new ArrayList<String>();
        cmd.add(getCommandPath("psql"));
        cmd.add("-l");

        if (username != null) {
            cmd.add("-U");
            cmd.add(username);
        }

        if (port != null) {
            cmd.add("-p");
            cmd.add(port.toString());
        }
        return cmd;
    }

    private List<String> createPostgresCommand() throws MojoExecutionException {
        List<String> cmd = new ArrayList<String>();
        cmd.add(getCommandPath("postgres"));

        cmd.add("-D");
        cmd.add(dataDir);

        if (port != null) {
            cmd.add("-p");
            cmd.add(port.toString());
        }

        if (parameters != null) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                cmd.add("-c");
                cmd.add(entry.getKey() + "=" + entry.getValue());
            }
        }

        return cmd;
    }

    private class FileLogger implements Runnable {

        private InputStream input;

        public FileLogger(InputStream input) {
            this.input = input;
        }

        public void run() {
            try (FileOutputStream log = new FileOutputStream(logfile)) {
                byte[] buffer = new byte[1024];
                int len = input.read(buffer);
                while (len != -1) {
                    log.write(buffer, 0, len);
                    log.flush();
                    len = input.read(buffer);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

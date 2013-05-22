package com.github.adrianboimvaser.postresql.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "start")
public class StartMojo extends PgctlMojo {

    private static final String SERVER_STARTED = "server started";

    @Parameter
    protected String log;
    
    @Parameter
    protected boolean quiet;

    @Override
    public void doExecute() throws MojoExecutionException {

        final List<String> cmd = createCommand();

        final ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.environment().put("LC_ALL", "en_US.UTF-8");

        try {
            getLog().info("Startig PostgreSQL");
            Process process = processBuilder.start();

            InputStream input = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input ));
            
            // Wait for pg_ctl to output "server started" before returning
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    getLog().info(line);
                    if (line.equals(SERVER_STARTED)) {
                        return;
                    }
                }
            } catch (IOException e) {
                getLog().error(e);
            }
        } catch (IOException e) {
            getLog().error(e);
        }
    }

    private List<String> createCommand() throws MojoExecutionException {
        List<String> cmd = new ArrayList<String>();
        cmd.add(getCommandPath("pg_ctl"));

        cmd.add("-D");
        cmd.add(dataDir);

        if (log == null) {
            log = dataDir + "\\server.log";
        }
        cmd.add("-l");
        cmd.add(log);
        
        if (quiet) {
            // Print only errors, no informational messages.
            cmd.add("-s");
        }

        // Wait for the startup or shutdown to complete.
        cmd.add("-w");

        cmd.add("start");

        return cmd;
    }
}

package org.postgresql;

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

    @Parameter
    protected String log;
    
    @Parameter
    protected boolean quiet;

    @Override
    public void doExecute() throws MojoExecutionException {

        final List<String> cmd = new ArrayList<String>();
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

        final ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        try {
            getLog().info("Startig PostgreSQL");
            Process process = processBuilder.start();

            InputStream input = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input ));
            
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    getLog().info(line);
                    //TODO: change to English, force the command to use English
                    if (line.equals("servidor iniciado")) {
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
}

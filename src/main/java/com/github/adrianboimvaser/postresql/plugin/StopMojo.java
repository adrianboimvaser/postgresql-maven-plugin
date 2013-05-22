package com.github.adrianboimvaser.postresql.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "stop")
public class StopMojo extends PgctlMojo {

    @Override
    public void doExecute() throws MojoExecutionException {

        final List<String> cmd = createCommand();

        final ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        try {
            Process process = processBuilder.start();
            logOutput(process);
            int returnValue = process.waitFor();
            getLog().debug("pg_ctl returned " + returnValue);
        } catch (IOException e) {
            getLog().error(e);
        } catch (InterruptedException e) {
            getLog().error(e);
        }
    }

    private List<String> createCommand() throws MojoExecutionException {
        List<String> cmd = new ArrayList<String>();
        cmd.add(getCommandPath("pg_ctl"));

        cmd.add("-D");
        cmd.add(dataDir);

        cmd.add("-m");
        cmd.add("fast");

        cmd.add("stop");

        return cmd;
    }
}

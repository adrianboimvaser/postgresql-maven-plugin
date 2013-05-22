package com.github.adrianboimvaser.postresql.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "createdb")
public class CreatedbMojo extends PgsqlMojo {

    @Parameter(required = true)
    protected String username;

    @Parameter(required = true)
    protected String databaseName;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().debug("Skipped.");
            return;
        }

        final List<String> cmd = createCommand();

        final ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        try {
            Process process = processBuilder.start();
            logOutput(process);
            int returnValue = process.waitFor();
            getLog().debug("createdb returned " + returnValue);
        } catch (IOException e) {
            getLog().error(e);
        } catch (InterruptedException e) {
            getLog().error(e);
        }
    }

    private List<String> createCommand() throws MojoExecutionException {
        List<String> cmd = new ArrayList<String>();
        cmd.add(getCommandPath("createdb"));

        cmd.add("-U");
        cmd.add(username);

        cmd.add(databaseName);

        return cmd;
    }
}

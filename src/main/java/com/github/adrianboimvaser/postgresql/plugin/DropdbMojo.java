package com.github.adrianboimvaser.postgresql.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "dropdb")
public class DropdbMojo extends PgsqlMojo {

    @Parameter
    protected String username;

    @Parameter(required = true)
    protected String databaseName;

    @Parameter
    protected String host;

    @Parameter
    protected Integer port;

    /** If specified, no error will be reported when dropping a non-existent database */
    @Parameter(alias = "if-exists", property = "if-exists", defaultValue = "false")
    protected boolean ifExists;

    /** If specified, password prompts are disabled (may result in an error) */
    @Parameter(alias = "no-password", property = "no-password", defaultValue = "false")
    protected boolean noPassword;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().debug("Skipped.");
            return;
        }

        final List<String> cmd = createCommand();
        if (getLog().isDebugEnabled()) {
            getLog().debug(cmd.toString());
        }

        final ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        String message = "";
        Exception cause = null;
        int returnValue = Integer.MIN_VALUE;
        try {
            Process process = processBuilder.start();
            logOutput(process);
            returnValue = process.waitFor();
            message = "dropdb returned " + returnValue;
            getLog().debug(message);
        } catch (IOException|InterruptedException e) {
            message = e.getLocalizedMessage();
            cause = e;
            getLog().error(e);
        }
        if (returnValue != 0 && failOnError) {
            throw new MojoExecutionException(message, cause);
        }
    }

    private List<String> createCommand() throws MojoExecutionException {
        List<String> cmd = new ArrayList<String>();
        cmd.add(getCommandPath("dropdb"));

        if (host != null) {
            cmd.add("-h");
            cmd.add(host);
        }

        if (port != null) {
            cmd.add("-p");
            cmd.add(port.toString());
        }

        if (username != null) {
            cmd.add("-U");
            cmd.add(username);
        }

        if (ifExists) {
            cmd.add("--if-exists");
        }

        if (noPassword) {
            cmd.add("--no-password");
        }

        cmd.add(databaseName);
        return cmd;
    }
}

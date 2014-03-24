package com.github.adrianboimvaser.postresql.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "initdb")
public class InitdbMojo extends PgsqlMojo {

    @Parameter(required = true)
    protected String dataDir;

    @Parameter
    protected String username;

    @Parameter
    protected String passwordFile;

	@Parameter
	protected String encoding;

	@Parameter
	protected String locale;

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
            getLog().debug("initdb returned " + returnValue);
        } catch (IOException e) {
            getLog().error(e);
        } catch (InterruptedException e) {
            getLog().error(e);
        }
    }

    private List<String> createCommand() throws MojoExecutionException {
        List<String> cmd = new ArrayList<String>();
        cmd.add(getCommandPath("initdb"));

        cmd.add("-D");
        cmd.add(dataDir);

        if (username != null) {
            cmd.add("-U");
            cmd.add(username);
        }

        if (passwordFile != null) {
            cmd.add("--pwfile");
            cmd.add(passwordFile);
        }

        if (encoding != null) {
            cmd.add("--encoding");
            cmd.add(encoding);
        }

        if (locale != null) {
            cmd.add("--locale");
            cmd.add(locale);
        }

        return cmd;
    }
}

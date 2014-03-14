package com.github.adrianboimvaser.postgresql.plugin;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class PgsqlMojo extends AbstractMojo {

    @Parameter(required = true)
    protected String pgsqlHome;

    @Parameter
    protected boolean skip;

    /** By default, most failed operations will not fail the build. This enables build failure. */
    @Parameter
    protected boolean failOnError;

    public PgsqlMojo() {
        //
    }

    public PgsqlMojo(String pgsqlHome) {
        this.pgsqlHome = pgsqlHome;
    }

    protected String getCommandPath(String command) throws MojoExecutionException {

        final File pgsqlHomeFile = new File(pgsqlHome);

        if (!pgsqlHomeFile.isDirectory()) {
            throw new MojoExecutionException(String.format(
                    "'%s' is not a valid directory.", pgsqlHome));
        }

        return new File(new File(pgsqlHome, "bin"), command).getAbsolutePath();
    }

    protected void logOutput(Process process) {
        StreamLogger outputLogger = new OutputLogger(getLog(), process.getInputStream());
        new Thread(outputLogger).run();
    
        StreamLogger errorLogger = new ErrorLogger(getLog(), process.getErrorStream());
        new Thread(errorLogger).run();
    }

}

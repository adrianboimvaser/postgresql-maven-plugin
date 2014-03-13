package com.github.adrianboimvaser.postresql.plugin;

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

    /**
     * Returns {@code true} if {@code arg} is not {@code null} and consists of
     * zero-or-more whitespace, or is case-insensitively "{@code true}".
     *
     * <p>
     * In the POM, it can be specified as {@code &lt;argname/&gt;} or
     * {@code &lt;argname&gt;  &lt;/argname&gt;} or
     * {@code &lt;argname&gt;  TRUE  &lt;/argname&gt;}, etc., all of which will cause
     * this method to return true. Non-whitespace values that are not equivalent to
     * the case-insensitive value {@code true} will cause this method to return false.
     * </p>
     *
     * <p>
     * Some properties may be specified on the command line (e.g. {@code mvn -Dargname},
     * and this method exists to explicitly support such usage.
     * </p>
     */
    protected static boolean trueBooleanString(String arg) {
        if (arg != null) {
            arg = arg.trim().toLowerCase();
            return "".equals(arg) || "true".equals(arg);
        }
        return false;
    }
}

package org.postgresql;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class PgctlMojo extends PgsqlMojo {

    @Parameter(required = true)
    protected String dataDir;

    @Parameter(defaultValue = "false")
    private boolean silent;

    @Parameter
    private Integer waitSeconds;

    @Parameter(defaultValue = "false")
    private boolean wait;

    public void execute() throws MojoExecutionException, MojoFailureException {
        doExecute();
    }

    protected abstract void doExecute() throws MojoExecutionException;
}

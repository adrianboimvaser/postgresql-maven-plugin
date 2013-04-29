package org.postgresql;

import java.io.File;
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

    @Parameter(required = true)
    protected String username;

    @Parameter(required = true)
    protected String passwordFile;

    public void execute() throws MojoExecutionException, MojoFailureException {
        final File pgsqlHomeFile = new File(pgsqlHome);

        if (!pgsqlHomeFile.isDirectory()) {
            throw new MojoExecutionException(String.format(
                    "'%s' is not a valid directory.", pgsqlHome));
        }

        final List<String> cmd = new ArrayList<String>();
        cmd.add(getCommandPath("initdb"));
        cmd.add("-D");
        cmd.add(dataDir);
        cmd.add("-U");
        cmd.add(username);
        cmd.add("--pwfile");
        cmd.add(passwordFile);

        final ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        try {
            Process process = processBuilder.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

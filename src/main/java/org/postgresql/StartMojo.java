package org.postgresql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "start")
public class StartMojo extends PgctlMojo {

    @Override
    public void doExecute() throws MojoExecutionException {

        final List<String> cmd = new ArrayList<String>();
        cmd.add(getCommandPath("pg_ctl"));

        cmd.add("-D");
        cmd.add(dataDir);

        cmd.add("-l");
        cmd.add(dataDir + "\\server.log");

        cmd.add("-w");

        cmd.add("start");

        final ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        try {
            getLog().info("Startig PostgreSQL");
            Process process = processBuilder.start();
            // TODO: there should be a way to run this process detached, maybe
            // watch the output stream and return when it says it's done.
            Thread.sleep(1000);
        } catch (IOException e) {
            getLog().error(e);
        } catch (InterruptedException e) {
            getLog().error(e);
        }
    }
}

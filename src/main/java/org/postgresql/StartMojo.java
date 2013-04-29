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
            Process process = processBuilder.start();
            Thread.sleep(500);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

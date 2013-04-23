package org.postgresql;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "stop")
public class StopMojo extends PgctlMojo {

    @Override
    public void doExecute() throws MojoExecutionException {
        final File pgsqlHomeFile = new File(pgsqlHome);

        if (!pgsqlHomeFile.isDirectory()) {
            throw new MojoExecutionException(String.format(
                    "'%s' is not a valid directory.", pgsqlHome));
        }

        final List<String> cmd = new ArrayList<String>();
        cmd.add(pgsqlHome + "\\bin\\pg_ctl.exe");
        cmd.add("-D");
        cmd.add(dataDir);

        cmd.add("-m");
        cmd.add("fast");

        cmd.add("stop");

        final ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        try {
            Process process = processBuilder.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

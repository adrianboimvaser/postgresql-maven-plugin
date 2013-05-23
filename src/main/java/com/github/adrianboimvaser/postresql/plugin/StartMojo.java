package com.github.adrianboimvaser.postresql.plugin;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "start")
public class StartMojo extends PgctlMojo {

    @Parameter
    private String logfile;

    @Parameter
    private Integer port;

    @Parameter
    private String username;

    @Override
    public void doExecute() throws MojoExecutionException {

        List<String> cmd = createPostgresCommand();

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);

        try {
            getLog().info("Startig PostgreSQL");
            Process process = processBuilder.start();
            destroyOnShutdown(process);

            // Wait for the server to start before returning
            while (!started()) {
                Thread.sleep(1000);
            }
            getLog().info("server started");

        } catch (Exception e) {
            getLog().error(e);
        }
    }

    private void destroyOnShutdown(final Process process) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                process.destroy();
            }
        });
    }

    private boolean started() {
        // As mentioned in http://www.postgresql.org/docs/9.0/static/app-pg-ctl.html
        // a successful psql -l indicates success.
        try {
            List<String> cmd = createPsqlCommand();
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();
            process.waitFor();
            return process.exitValue() == 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> createPsqlCommand() throws MojoExecutionException {
        List<String> cmd = new ArrayList<String>();
        cmd.add(getCommandPath("psql"));
        cmd.add("-l");

        if (username != null) {
            cmd.add("-U");
            cmd.add(username);
        }

        if (port != null) {
            cmd.add("-p");
            cmd.add(port.toString());
        }
        return cmd;
    }

    private List<String> createPostgresCommand() throws MojoExecutionException {
        List<String> cmd = new ArrayList<String>();
        cmd.add(getCommandPath("postgres"));

        cmd.add("-D");
        cmd.add(dataDir);

        if (logfile != null) {
            cmd.add("-r");
            cmd.add(logfile);
        }

        if (port != null) {
            cmd.add("-p");
            cmd.add(port.toString());
        }

        return cmd;
    }
}

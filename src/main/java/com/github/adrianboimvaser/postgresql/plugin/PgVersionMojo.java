package com.github.adrianboimvaser.postgresql.plugin;

import com.github.adrianboimvaser.postgresql.plugin.enforcer.RequirePostgresqlVersion;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.IOUtil;

import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Logs the active PostgreSQL version and optionally enforces a minimum version. The optional
 * dependency {@code maven-enforcer-plugin} is required for enforcement to work.
 *
 * <p><a href="http://maven.apache.org/enforcer/index.html">http://maven.apache.org/enforcer/index.html</a></p>
 */
@Mojo(name = "version")
public class PgVersionMojo extends PgsqlMojo {

    static final String PATTERN = "psql \\(PostgreSQL\\) ([0-9]+\\.[0-9]+\\.[0-9]+)";

    /**
     * When specified, the build will fail if the available Postgresql version does not comply with the
     * given version range specification. Based on (and implemented using) Maven Enforcer. See
     * <a href="http://maven.apache.org/enforcer/enforcer-rules/versionRanges.html"
     * >Version Range Specification</a>.
     */
    @Parameter
    private String mandatoryVersionRange;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            return;
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(getCommandPath("psql"), "--version");
            final Process process = processBuilder.start();
            process.waitFor();
            if (process.exitValue() != 0) {
                throw new MojoExecutionException("psql return code " + process.exitValue());
            }

            String result = "";
            try (final InputStreamReader isr = new InputStreamReader(process.getInputStream())) {
                result = IOUtil.toString(isr);
                getLog().info(result);
            }
            if (mandatoryVersionRange != null) {
                final Matcher matcher = Pattern.compile(PATTERN).matcher(result);
                if (matcher.groupCount() != 1) {
                    getLog().warn("Unable to match version number");
                    return;
                }
                final String version = matcher.replaceAll("$1");
                RequirePostgresqlVersion.Builder.matchRange(mandatoryVersionRange)
                        .withVersion(version)
                        .build()
                        .execute(getLog());
            }
        } catch (MojoExecutionException e) {
            throw e;
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}

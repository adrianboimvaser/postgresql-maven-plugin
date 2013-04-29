package org.postgresql;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class PgsqlMojo extends AbstractMojo {

    @Parameter(required = true)
    protected String pgsqlHome;

	protected String getCommandPath(String command) {
		return new File(new File(pgsqlHome, "bin"), command).getAbsolutePath();
	}

}

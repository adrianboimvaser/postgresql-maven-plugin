package org.postgresql;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class PgsqlMojo extends AbstractMojo {

    @Parameter(required = true)
    protected String pgsqlHome;

}

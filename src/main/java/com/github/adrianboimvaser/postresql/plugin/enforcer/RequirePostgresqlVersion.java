package com.github.adrianboimvaser.postresql.plugin.enforcer;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.enforcer.AbstractVersionEnforcer;

public class RequirePostgresqlVersion extends AbstractVersionEnforcer {

    private final String requiredVersionRange;
    private final DefaultArtifactVersion version;

    RequirePostgresqlVersion(String requiredVersionRange, String version) {
        this.requiredVersionRange = requiredVersionRange;
        this.version = new DefaultArtifactVersion(version);
    }

    public void execute(Log log) throws EnforcerRuleException {
        enforceVersion(log, "PostgreSQL", requiredVersionRange, version);
    }

    @Override
    public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {
        execute(helper.getLog());
    }

    public static interface WithVersion {
        RequirePostgresqlVersion build();
    }

    public static interface WithRange {
        WithVersion withVersion(String version);
    }

    public static final class Builder implements WithVersion, WithRange {
        private String range;
        private String version;

        private Builder(String range) {
            this.range = range;
        }

        public static WithRange matchRange(String range) {
            return new Builder(range);
        }

        @Override
        public WithVersion withVersion(String version) {
            this.version = version;
            return this;
        }

        @Override
        public RequirePostgresqlVersion build() {
            return new RequirePostgresqlVersion(range, version);
        }
    }
}

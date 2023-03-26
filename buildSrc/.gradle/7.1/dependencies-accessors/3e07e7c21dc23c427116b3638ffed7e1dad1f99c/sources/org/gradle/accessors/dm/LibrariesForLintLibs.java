package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `lintLibs` extension.
*/
@NonNullApi
public class LibrariesForLintLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final LintLibraryAccessors laccForLintLibraryAccessors = new LintLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(providers, config);

    @Inject
    public LibrariesForLintLibs(DefaultVersionCatalog config, ProviderFactory providers) {
        super(config, providers);
    }

    /**
     * Returns the group of libraries at lint
     */
    public LintLibraryAccessors getLint() { return laccForLintLibraryAccessors; }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() { return vaccForVersionAccessors; }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() { return baccForBundleAccessors; }

    public static class LintLibraryAccessors extends SubDependencyFactory {

        public LintLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for api (com.android.tools.lint:lint-api)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getApi() { return create("lint-api"); }

            /**
             * Creates a dependency provider for checks (com.android.tools.lint:lint-checks)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getChecks() { return create("lint-checks"); }

            /**
             * Creates a dependency provider for tests (com.android.tools.lint:lint-tests)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getTests() { return create("lint-tests"); }

    }

    public static class VersionAccessors extends VersionFactory {

        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: lint (30.2.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in script '../dependencies.gradle'
             */
            public Provider<String> getLint() { return getVersion("lint"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}

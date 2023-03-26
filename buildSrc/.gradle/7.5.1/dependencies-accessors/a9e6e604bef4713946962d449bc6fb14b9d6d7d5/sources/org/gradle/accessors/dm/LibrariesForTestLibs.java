package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `testLibs` extension.
*/
@NonNullApi
public class LibrariesForTestLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AndroidxLibraryAccessors laccForAndroidxLibraryAccessors = new AndroidxLibraryAccessors(owner);
    private final AssertjLibraryAccessors laccForAssertjLibraryAccessors = new AssertjLibraryAccessors(owner);
    private final BouncycastleLibraryAccessors laccForBouncycastleLibraryAccessors = new BouncycastleLibraryAccessors(owner);
    private final ConscryptLibraryAccessors laccForConscryptLibraryAccessors = new ConscryptLibraryAccessors(owner);
    private final EspressoLibraryAccessors laccForEspressoLibraryAccessors = new EspressoLibraryAccessors(owner);
    private final HamcrestLibraryAccessors laccForHamcrestLibraryAccessors = new HamcrestLibraryAccessors(owner);
    private final JunitLibraryAccessors laccForJunitLibraryAccessors = new JunitLibraryAccessors(owner);
    private final MockitoLibraryAccessors laccForMockitoLibraryAccessors = new MockitoLibraryAccessors(owner);
    private final RobolectricLibraryAccessors laccForRobolectricLibraryAccessors = new RobolectricLibraryAccessors(owner);
    private final SquareLibraryAccessors laccForSquareLibraryAccessors = new SquareLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(providers, config);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForTestLibs(DefaultVersionCatalog config, ProviderFactory providers) {
        super(config, providers);
    }

    /**
     * Returns the group of libraries at androidx
     */
    public AndroidxLibraryAccessors getAndroidx() { return laccForAndroidxLibraryAccessors; }

    /**
     * Returns the group of libraries at assertj
     */
    public AssertjLibraryAccessors getAssertj() { return laccForAssertjLibraryAccessors; }

    /**
     * Returns the group of libraries at bouncycastle
     */
    public BouncycastleLibraryAccessors getBouncycastle() { return laccForBouncycastleLibraryAccessors; }

    /**
     * Returns the group of libraries at conscrypt
     */
    public ConscryptLibraryAccessors getConscrypt() { return laccForConscryptLibraryAccessors; }

    /**
     * Returns the group of libraries at espresso
     */
    public EspressoLibraryAccessors getEspresso() { return laccForEspressoLibraryAccessors; }

    /**
     * Returns the group of libraries at hamcrest
     */
    public HamcrestLibraryAccessors getHamcrest() { return laccForHamcrestLibraryAccessors; }

    /**
     * Returns the group of libraries at junit
     */
    public JunitLibraryAccessors getJunit() { return laccForJunitLibraryAccessors; }

    /**
     * Returns the group of libraries at mockito
     */
    public MockitoLibraryAccessors getMockito() { return laccForMockitoLibraryAccessors; }

    /**
     * Returns the group of libraries at robolectric
     */
    public RobolectricLibraryAccessors getRobolectric() { return laccForRobolectricLibraryAccessors; }

    /**
     * Returns the group of libraries at square
     */
    public SquareLibraryAccessors getSquare() { return laccForSquareLibraryAccessors; }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() { return vaccForVersionAccessors; }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() { return baccForBundleAccessors; }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() { return paccForPluginAccessors; }

    public static class AndroidxLibraryAccessors extends SubDependencyFactory {
        private final AndroidxTestLibraryAccessors laccForAndroidxTestLibraryAccessors = new AndroidxTestLibraryAccessors(owner);

        public AndroidxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at androidx.test
         */
        public AndroidxTestLibraryAccessors getTest() { return laccForAndroidxTestLibraryAccessors; }

    }

    public static class AndroidxTestLibraryAccessors extends SubDependencyFactory {
        private final AndroidxTestCoreLibraryAccessors laccForAndroidxTestCoreLibraryAccessors = new AndroidxTestCoreLibraryAccessors(owner);
        private final AndroidxTestExtLibraryAccessors laccForAndroidxTestExtLibraryAccessors = new AndroidxTestExtLibraryAccessors(owner);

        public AndroidxTestLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for orchestrator (androidx.test:orchestrator)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getOrchestrator() { return create("androidx.test.orchestrator"); }

        /**
         * Returns the group of libraries at androidx.test.core
         */
        public AndroidxTestCoreLibraryAccessors getCore() { return laccForAndroidxTestCoreLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.test.ext
         */
        public AndroidxTestExtLibraryAccessors getExt() { return laccForAndroidxTestExtLibraryAccessors; }

    }

    public static class AndroidxTestCoreLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AndroidxTestCoreLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (androidx.test:core)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("androidx.test.core"); }

            /**
             * Creates a dependency provider for ktx (androidx.test:core-ktx)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx.test.core.ktx"); }

    }

    public static class AndroidxTestExtLibraryAccessors extends SubDependencyFactory {
        private final AndroidxTestExtJunitLibraryAccessors laccForAndroidxTestExtJunitLibraryAccessors = new AndroidxTestExtJunitLibraryAccessors(owner);

        public AndroidxTestExtLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at androidx.test.ext.junit
         */
        public AndroidxTestExtJunitLibraryAccessors getJunit() { return laccForAndroidxTestExtJunitLibraryAccessors; }

    }

    public static class AndroidxTestExtJunitLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AndroidxTestExtJunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for junit (androidx.test.ext:junit)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("androidx.test.ext.junit"); }

            /**
             * Creates a dependency provider for ktx (androidx.test.ext:junit-ktx)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx.test.ext.junit.ktx"); }

    }

    public static class AssertjLibraryAccessors extends SubDependencyFactory {

        public AssertjLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (org.assertj:assertj-core)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("assertj.core"); }

    }

    public static class BouncycastleLibraryAccessors extends SubDependencyFactory {
        private final BouncycastleBcprovLibraryAccessors laccForBouncycastleBcprovLibraryAccessors = new BouncycastleBcprovLibraryAccessors(owner);

        public BouncycastleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at bouncycastle.bcprov
         */
        public BouncycastleBcprovLibraryAccessors getBcprov() { return laccForBouncycastleBcprovLibraryAccessors; }

    }

    public static class BouncycastleBcprovLibraryAccessors extends SubDependencyFactory {

        public BouncycastleBcprovLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for jdk15on (org.bouncycastle:bcprov-jdk15on)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getJdk15on() { return create("bouncycastle.bcprov.jdk15on"); }

    }

    public static class ConscryptLibraryAccessors extends SubDependencyFactory {
        private final ConscryptOpenjdkLibraryAccessors laccForConscryptOpenjdkLibraryAccessors = new ConscryptOpenjdkLibraryAccessors(owner);

        public ConscryptLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at conscrypt.openjdk
         */
        public ConscryptOpenjdkLibraryAccessors getOpenjdk() { return laccForConscryptOpenjdkLibraryAccessors; }

    }

    public static class ConscryptOpenjdkLibraryAccessors extends SubDependencyFactory {

        public ConscryptOpenjdkLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for uber (org.conscrypt:conscrypt-openjdk-uber)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getUber() { return create("conscrypt.openjdk.uber"); }

    }

    public static class EspressoLibraryAccessors extends SubDependencyFactory {

        public EspressoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (androidx.test.espresso:espresso-core)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("espresso.core"); }

    }

    public static class HamcrestLibraryAccessors extends SubDependencyFactory {

        public HamcrestLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for hamcrest (org.hamcrest:hamcrest)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getHamcrest() { return create("hamcrest.hamcrest"); }

    }

    public static class JunitLibraryAccessors extends SubDependencyFactory {

        public JunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for junit (junit:junit)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getJunit() { return create("junit.junit"); }

    }

    public static class MockitoLibraryAccessors extends SubDependencyFactory {

        public MockitoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (org.mockito:mockito-android)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getAndroid() { return create("mockito.android"); }

            /**
             * Creates a dependency provider for core (org.mockito:mockito-inline)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("mockito.core"); }

            /**
             * Creates a dependency provider for kotlin (org.mockito.kotlin:mockito-kotlin)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getKotlin() { return create("mockito.kotlin"); }

    }

    public static class RobolectricLibraryAccessors extends SubDependencyFactory {
        private final RobolectricShadowsLibraryAccessors laccForRobolectricShadowsLibraryAccessors = new RobolectricShadowsLibraryAccessors(owner);

        public RobolectricLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for robolectric (org.robolectric:robolectric)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getRobolectric() { return create("robolectric.robolectric"); }

        /**
         * Returns the group of libraries at robolectric.shadows
         */
        public RobolectricShadowsLibraryAccessors getShadows() { return laccForRobolectricShadowsLibraryAccessors; }

    }

    public static class RobolectricShadowsLibraryAccessors extends SubDependencyFactory {

        public RobolectricShadowsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for multidex (org.robolectric:shadows-multidex)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getMultidex() { return create("robolectric.shadows.multidex"); }

    }

    public static class SquareLibraryAccessors extends SubDependencyFactory {
        private final SquareOkhttpLibraryAccessors laccForSquareOkhttpLibraryAccessors = new SquareOkhttpLibraryAccessors(owner);

        public SquareLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at square.okhttp
         */
        public SquareOkhttpLibraryAccessors getOkhttp() { return laccForSquareOkhttpLibraryAccessors; }

    }

    public static class SquareOkhttpLibraryAccessors extends SubDependencyFactory {

        public SquareOkhttpLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for mockserver (com.squareup.okhttp3:mockwebserver)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getMockserver() { return create("square.okhttp.mockserver"); }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final AndroidxVersionAccessors vaccForAndroidxVersionAccessors = new AndroidxVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: robolectric (4.8.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in script '../dependencies.gradle'
             */
            public Provider<String> getRobolectric() { return getVersion("robolectric"); }

        /**
         * Returns the group of versions at versions.androidx
         */
        public AndroidxVersionAccessors getAndroidx() { return vaccForAndroidxVersionAccessors; }

    }

    public static class AndroidxVersionAccessors extends VersionFactory  {

        private final AndroidxTestVersionAccessors vaccForAndroidxTestVersionAccessors = new AndroidxTestVersionAccessors(providers, config);
        public AndroidxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.androidx.test
         */
        public AndroidxTestVersionAccessors getTest() { return vaccForAndroidxTestVersionAccessors; }

    }

    public static class AndroidxTestVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        private final AndroidxTestExtVersionAccessors vaccForAndroidxTestExtVersionAccessors = new AndroidxTestExtVersionAccessors(providers, config);
        public AndroidxTestVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the version associated to this alias: androidx.test (1.4.0)
         * If the version is a rich version and that its not expressible as a
         * single version string, then an empty string is returned.
         * This version was declared in script '../dependencies.gradle'
         */
        public Provider<String> asProvider() { return getVersion("androidx.test"); }

        /**
         * Returns the group of versions at versions.androidx.test.ext
         */
        public AndroidxTestExtVersionAccessors getExt() { return vaccForAndroidxTestExtVersionAccessors; }

    }

    public static class AndroidxTestExtVersionAccessors extends VersionFactory  {

        public AndroidxTestExtVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: androidx.test.ext.junit (1.1.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in script '../dependencies.gradle'
             */
            public Provider<String> getJunit() { return getVersion("androidx.test.ext.junit"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}

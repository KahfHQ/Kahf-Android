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
 * A catalog of dependencies accessible via the `libs` extension.
*/
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AndroidLibraryAccessors laccForAndroidLibraryAccessors = new AndroidLibraryAccessors(owner);
    private final AndroidxLibraryAccessors laccForAndroidxLibraryAccessors = new AndroidxLibraryAccessors(owner);
    private final ApacheLibraryAccessors laccForApacheLibraryAccessors = new ApacheLibraryAccessors(owner);
    private final ConscryptLibraryAccessors laccForConscryptLibraryAccessors = new ConscryptLibraryAccessors(owner);
    private final EmilsjolanderLibraryAccessors laccForEmilsjolanderLibraryAccessors = new EmilsjolanderLibraryAccessors(owner);
    private final ExoplayerLibraryAccessors laccForExoplayerLibraryAccessors = new ExoplayerLibraryAccessors(owner);
    private final FirebaseLibraryAccessors laccForFirebaseLibraryAccessors = new FirebaseLibraryAccessors(owner);
    private final GlideLibraryAccessors laccForGlideLibraryAccessors = new GlideLibraryAccessors(owner);
    private final GoogleLibraryAccessors laccForGoogleLibraryAccessors = new GoogleLibraryAccessors(owner);
    private final GreenrobotLibraryAccessors laccForGreenrobotLibraryAccessors = new GreenrobotLibraryAccessors(owner);
    private final JacksonLibraryAccessors laccForJacksonLibraryAccessors = new JacksonLibraryAccessors(owner);
    private final JknackLibraryAccessors laccForJknackLibraryAccessors = new JknackLibraryAccessors(owner);
    private final JpardogoLibraryAccessors laccForJpardogoLibraryAccessors = new JpardogoLibraryAccessors(owner);
    private final KotlinLibraryAccessors laccForKotlinLibraryAccessors = new KotlinLibraryAccessors(owner);
    private final LeolinLibraryAccessors laccForLeolinLibraryAccessors = new LeolinLibraryAccessors(owner);
    private final LibsignalLibraryAccessors laccForLibsignalLibraryAccessors = new LibsignalLibraryAccessors(owner);
    private final MaterialLibraryAccessors laccForMaterialLibraryAccessors = new MaterialLibraryAccessors(owner);
    private final MaterialishLibraryAccessors laccForMaterialishLibraryAccessors = new MaterialishLibraryAccessors(owner);
    private final Mp4parserLibraryAccessors laccForMp4parserLibraryAccessors = new Mp4parserLibraryAccessors(owner);
    private final NanohttpdLibraryAccessors laccForNanohttpdLibraryAccessors = new NanohttpdLibraryAccessors(owner);
    private final Rxjava3LibraryAccessors laccForRxjava3LibraryAccessors = new Rxjava3LibraryAccessors(owner);
    private final SignalLibraryAccessors laccForSignalLibraryAccessors = new SignalLibraryAccessors(owner);
    private final SquareLibraryAccessors laccForSquareLibraryAccessors = new SquareLibraryAccessors(owner);
    private final SubsamplingLibraryAccessors laccForSubsamplingLibraryAccessors = new SubsamplingLibraryAccessors(owner);
    private final TimeLibraryAccessors laccForTimeLibraryAccessors = new TimeLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers) {
        super(config, providers);
    }

        /**
         * Creates a dependency provider for colorpicker (com.takisoft.fix:colorpicker)
         * This dependency was declared in script '../dependencies.gradle'
         */
        public Provider<MinimalExternalModuleDependency> getColorpicker() { return create("colorpicker"); }

        /**
         * Creates a dependency provider for dnsjava (dnsjava:dnsjava)
         * This dependency was declared in script '../dependencies.gradle'
         */
        public Provider<MinimalExternalModuleDependency> getDnsjava() { return create("dnsjava"); }

        /**
         * Creates a dependency provider for lottie (com.airbnb.android:lottie)
         * This dependency was declared in script '../dependencies.gradle'
         */
        public Provider<MinimalExternalModuleDependency> getLottie() { return create("lottie"); }

        /**
         * Creates a dependency provider for mobilecoin (com.mobilecoin:android-sdk)
         * This dependency was declared in script '../dependencies.gradle'
         */
        public Provider<MinimalExternalModuleDependency> getMobilecoin() { return create("mobilecoin"); }

        /**
         * Creates a dependency provider for numberpickerview (cn.carbswang.android:NumberPickerView)
         * This dependency was declared in script '../dependencies.gradle'
         */
        public Provider<MinimalExternalModuleDependency> getNumberpickerview() { return create("numberpickerview"); }

        /**
         * Creates a dependency provider for photoview (com.github.chrisbanes:PhotoView)
         * This dependency was declared in script '../dependencies.gradle'
         */
        public Provider<MinimalExternalModuleDependency> getPhotoview() { return create("photoview"); }

        /**
         * Creates a dependency provider for roundedimageview (com.makeramen:roundedimageview)
         * This dependency was declared in script '../dependencies.gradle'
         */
        public Provider<MinimalExternalModuleDependency> getRoundedimageview() { return create("roundedimageview"); }

        /**
         * Creates a dependency provider for rxdogtag (com.uber.rxdogtag2:rxdogtag)
         * This dependency was declared in script '../dependencies.gradle'
         */
        public Provider<MinimalExternalModuleDependency> getRxdogtag() { return create("rxdogtag"); }

        /**
         * Creates a dependency provider for stickyheadergrid (com.codewaves.stickyheadergrid:stickyheadergrid)
         * This dependency was declared in script '../dependencies.gradle'
         */
        public Provider<MinimalExternalModuleDependency> getStickyheadergrid() { return create("stickyheadergrid"); }

        /**
         * Creates a dependency provider for stream (com.annimon:stream)
         * This dependency was declared in script '../dependencies.gradle'
         */
        public Provider<MinimalExternalModuleDependency> getStream() { return create("stream"); }

        /**
         * Creates a dependency provider for waitingdots (pl.tajchert:waitingdots)
         * This dependency was declared in script '../dependencies.gradle'
         */
        public Provider<MinimalExternalModuleDependency> getWaitingdots() { return create("waitingdots"); }

    /**
     * Returns the group of libraries at android
     */
    public AndroidLibraryAccessors getAndroid() { return laccForAndroidLibraryAccessors; }

    /**
     * Returns the group of libraries at androidx
     */
    public AndroidxLibraryAccessors getAndroidx() { return laccForAndroidxLibraryAccessors; }

    /**
     * Returns the group of libraries at apache
     */
    public ApacheLibraryAccessors getApache() { return laccForApacheLibraryAccessors; }

    /**
     * Returns the group of libraries at conscrypt
     */
    public ConscryptLibraryAccessors getConscrypt() { return laccForConscryptLibraryAccessors; }

    /**
     * Returns the group of libraries at emilsjolander
     */
    public EmilsjolanderLibraryAccessors getEmilsjolander() { return laccForEmilsjolanderLibraryAccessors; }

    /**
     * Returns the group of libraries at exoplayer
     */
    public ExoplayerLibraryAccessors getExoplayer() { return laccForExoplayerLibraryAccessors; }

    /**
     * Returns the group of libraries at firebase
     */
    public FirebaseLibraryAccessors getFirebase() { return laccForFirebaseLibraryAccessors; }

    /**
     * Returns the group of libraries at glide
     */
    public GlideLibraryAccessors getGlide() { return laccForGlideLibraryAccessors; }

    /**
     * Returns the group of libraries at google
     */
    public GoogleLibraryAccessors getGoogle() { return laccForGoogleLibraryAccessors; }

    /**
     * Returns the group of libraries at greenrobot
     */
    public GreenrobotLibraryAccessors getGreenrobot() { return laccForGreenrobotLibraryAccessors; }

    /**
     * Returns the group of libraries at jackson
     */
    public JacksonLibraryAccessors getJackson() { return laccForJacksonLibraryAccessors; }

    /**
     * Returns the group of libraries at jknack
     */
    public JknackLibraryAccessors getJknack() { return laccForJknackLibraryAccessors; }

    /**
     * Returns the group of libraries at jpardogo
     */
    public JpardogoLibraryAccessors getJpardogo() { return laccForJpardogoLibraryAccessors; }

    /**
     * Returns the group of libraries at kotlin
     */
    public KotlinLibraryAccessors getKotlin() { return laccForKotlinLibraryAccessors; }

    /**
     * Returns the group of libraries at leolin
     */
    public LeolinLibraryAccessors getLeolin() { return laccForLeolinLibraryAccessors; }

    /**
     * Returns the group of libraries at libsignal
     */
    public LibsignalLibraryAccessors getLibsignal() { return laccForLibsignalLibraryAccessors; }

    /**
     * Returns the group of libraries at material
     */
    public MaterialLibraryAccessors getMaterial() { return laccForMaterialLibraryAccessors; }

    /**
     * Returns the group of libraries at materialish
     */
    public MaterialishLibraryAccessors getMaterialish() { return laccForMaterialishLibraryAccessors; }

    /**
     * Returns the group of libraries at mp4parser
     */
    public Mp4parserLibraryAccessors getMp4parser() { return laccForMp4parserLibraryAccessors; }

    /**
     * Returns the group of libraries at nanohttpd
     */
    public NanohttpdLibraryAccessors getNanohttpd() { return laccForNanohttpdLibraryAccessors; }

    /**
     * Returns the group of libraries at rxjava3
     */
    public Rxjava3LibraryAccessors getRxjava3() { return laccForRxjava3LibraryAccessors; }

    /**
     * Returns the group of libraries at signal
     */
    public SignalLibraryAccessors getSignal() { return laccForSignalLibraryAccessors; }

    /**
     * Returns the group of libraries at square
     */
    public SquareLibraryAccessors getSquare() { return laccForSquareLibraryAccessors; }

    /**
     * Returns the group of libraries at subsampling
     */
    public SubsamplingLibraryAccessors getSubsampling() { return laccForSubsamplingLibraryAccessors; }

    /**
     * Returns the group of libraries at time
     */
    public TimeLibraryAccessors getTime() { return laccForTimeLibraryAccessors; }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() { return vaccForVersionAccessors; }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() { return baccForBundleAccessors; }

    public static class AndroidLibraryAccessors extends SubDependencyFactory {
        private final AndroidToolsLibraryAccessors laccForAndroidToolsLibraryAccessors = new AndroidToolsLibraryAccessors(owner);

        public AndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for smsmms (com.klinkerapps:android-smsmms)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getSmsmms() { return create("android-smsmms"); }

            /**
             * Creates a dependency provider for tooltips (com.tomergoldst.android:tooltips)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getTooltips() { return create("android-tooltips"); }

        /**
         * Returns the group of libraries at android.tools
         */
        public AndroidToolsLibraryAccessors getTools() { return laccForAndroidToolsLibraryAccessors; }

    }

    public static class AndroidToolsLibraryAccessors extends SubDependencyFactory {

        public AndroidToolsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for desugar (com.android.tools:desugar_jdk_libs)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getDesugar() { return create("android-tools-desugar"); }

    }

    public static class AndroidxLibraryAccessors extends SubDependencyFactory {
        private final AndroidxActivityLibraryAccessors laccForAndroidxActivityLibraryAccessors = new AndroidxActivityLibraryAccessors(owner);
        private final AndroidxCameraLibraryAccessors laccForAndroidxCameraLibraryAccessors = new AndroidxCameraLibraryAccessors(owner);
        private final AndroidxConcurrentLibraryAccessors laccForAndroidxConcurrentLibraryAccessors = new AndroidxConcurrentLibraryAccessors(owner);
        private final AndroidxCoreLibraryAccessors laccForAndroidxCoreLibraryAccessors = new AndroidxCoreLibraryAccessors(owner);
        private final AndroidxFragmentLibraryAccessors laccForAndroidxFragmentLibraryAccessors = new AndroidxFragmentLibraryAccessors(owner);
        private final AndroidxLegacyLibraryAccessors laccForAndroidxLegacyLibraryAccessors = new AndroidxLegacyLibraryAccessors(owner);
        private final AndroidxLifecycleLibraryAccessors laccForAndroidxLifecycleLibraryAccessors = new AndroidxLifecycleLibraryAccessors(owner);
        private final AndroidxNavigationLibraryAccessors laccForAndroidxNavigationLibraryAccessors = new AndroidxNavigationLibraryAccessors(owner);
        private final AndroidxWindowLibraryAccessors laccForAndroidxWindowLibraryAccessors = new AndroidxWindowLibraryAccessors(owner);

        public AndroidxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for annotation (androidx.annotation:annotation)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getAnnotation() { return create("androidx-annotation"); }

            /**
             * Creates a dependency provider for appcompat (androidx.appcompat:appcompat)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getAppcompat() { return create("androidx-appcompat"); }

            /**
             * Creates a dependency provider for autofill (androidx.autofill:autofill)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getAutofill() { return create("androidx-autofill"); }

            /**
             * Creates a dependency provider for biometric (androidx.biometric:biometric)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getBiometric() { return create("androidx-biometric"); }

            /**
             * Creates a dependency provider for cardview (androidx.cardview:cardview)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getCardview() { return create("androidx-cardview"); }

            /**
             * Creates a dependency provider for constraintlayout (androidx.constraintlayout:constraintlayout)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getConstraintlayout() { return create("androidx-constraintlayout"); }

            /**
             * Creates a dependency provider for exifinterface (androidx.exifinterface:exifinterface)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getExifinterface() { return create("androidx-exifinterface"); }

            /**
             * Creates a dependency provider for gridlayout (androidx.gridlayout:gridlayout)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getGridlayout() { return create("androidx-gridlayout"); }

            /**
             * Creates a dependency provider for multidex (androidx.multidex:multidex)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getMultidex() { return create("androidx-multidex"); }

            /**
             * Creates a dependency provider for preference (androidx.preference:preference)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getPreference() { return create("androidx-preference"); }

            /**
             * Creates a dependency provider for recyclerview (androidx.recyclerview:recyclerview)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getRecyclerview() { return create("androidx-recyclerview"); }

            /**
             * Creates a dependency provider for sharetarget (androidx.sharetarget:sharetarget)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getSharetarget() { return create("androidx-sharetarget"); }

            /**
             * Creates a dependency provider for sqlite (androidx.sqlite:sqlite)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getSqlite() { return create("androidx-sqlite"); }

        /**
         * Returns the group of libraries at androidx.activity
         */
        public AndroidxActivityLibraryAccessors getActivity() { return laccForAndroidxActivityLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.camera
         */
        public AndroidxCameraLibraryAccessors getCamera() { return laccForAndroidxCameraLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.concurrent
         */
        public AndroidxConcurrentLibraryAccessors getConcurrent() { return laccForAndroidxConcurrentLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.core
         */
        public AndroidxCoreLibraryAccessors getCore() { return laccForAndroidxCoreLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.fragment
         */
        public AndroidxFragmentLibraryAccessors getFragment() { return laccForAndroidxFragmentLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.legacy
         */
        public AndroidxLegacyLibraryAccessors getLegacy() { return laccForAndroidxLegacyLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.lifecycle
         */
        public AndroidxLifecycleLibraryAccessors getLifecycle() { return laccForAndroidxLifecycleLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.navigation
         */
        public AndroidxNavigationLibraryAccessors getNavigation() { return laccForAndroidxNavigationLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.window
         */
        public AndroidxWindowLibraryAccessors getWindow() { return laccForAndroidxWindowLibraryAccessors; }

    }

    public static class AndroidxActivityLibraryAccessors extends SubDependencyFactory {

        public AndroidxActivityLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ktx (androidx.activity:activity-ktx)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx-activity-ktx"); }

    }

    public static class AndroidxCameraLibraryAccessors extends SubDependencyFactory {

        public AndroidxCameraLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for camera2 (androidx.camera:camera-camera2)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getCamera2() { return create("androidx-camera-camera2"); }

            /**
             * Creates a dependency provider for core (androidx.camera:camera-core)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("androidx-camera-core"); }

            /**
             * Creates a dependency provider for lifecycle (androidx.camera:camera-lifecycle)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getLifecycle() { return create("androidx-camera-lifecycle"); }

            /**
             * Creates a dependency provider for view (androidx.camera:camera-view)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getView() { return create("androidx-camera-view"); }

    }

    public static class AndroidxConcurrentLibraryAccessors extends SubDependencyFactory {

        public AndroidxConcurrentLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for futures (androidx.concurrent:concurrent-futures)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getFutures() { return create("androidx-concurrent-futures"); }

    }

    public static class AndroidxCoreLibraryAccessors extends SubDependencyFactory {

        public AndroidxCoreLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ktx (androidx.core:core-ktx)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx-core-ktx"); }

            /**
             * Creates a dependency provider for role (androidx.core:core-role)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getRole() { return create("androidx-core-role"); }

    }

    public static class AndroidxFragmentLibraryAccessors extends SubDependencyFactory {

        public AndroidxFragmentLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ktx (androidx.fragment:fragment-ktx)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx-fragment-ktx"); }

            /**
             * Creates a dependency provider for testing (androidx.fragment:fragment-testing)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getTesting() { return create("androidx-fragment-testing"); }

    }

    public static class AndroidxLegacyLibraryAccessors extends SubDependencyFactory {

        public AndroidxLegacyLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for preference (androidx.legacy:legacy-preference-v14)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getPreference() { return create("androidx-legacy-preference"); }

            /**
             * Creates a dependency provider for support (androidx.legacy:legacy-support-v13)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getSupport() { return create("androidx-legacy-support"); }

    }

    public static class AndroidxLifecycleLibraryAccessors extends SubDependencyFactory {
        private final AndroidxLifecycleCommonLibraryAccessors laccForAndroidxLifecycleCommonLibraryAccessors = new AndroidxLifecycleCommonLibraryAccessors(owner);
        private final AndroidxLifecycleLivedataLibraryAccessors laccForAndroidxLifecycleLivedataLibraryAccessors = new AndroidxLifecycleLivedataLibraryAccessors(owner);
        private final AndroidxLifecycleReactivestreamsLibraryAccessors laccForAndroidxLifecycleReactivestreamsLibraryAccessors = new AndroidxLifecycleReactivestreamsLibraryAccessors(owner);
        private final AndroidxLifecycleViewmodelLibraryAccessors laccForAndroidxLifecycleViewmodelLibraryAccessors = new AndroidxLifecycleViewmodelLibraryAccessors(owner);

        public AndroidxLifecycleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for process (androidx.lifecycle:lifecycle-process)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getProcess() { return create("androidx-lifecycle-process"); }

        /**
         * Returns the group of libraries at androidx.lifecycle.common
         */
        public AndroidxLifecycleCommonLibraryAccessors getCommon() { return laccForAndroidxLifecycleCommonLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.lifecycle.livedata
         */
        public AndroidxLifecycleLivedataLibraryAccessors getLivedata() { return laccForAndroidxLifecycleLivedataLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.lifecycle.reactivestreams
         */
        public AndroidxLifecycleReactivestreamsLibraryAccessors getReactivestreams() { return laccForAndroidxLifecycleReactivestreamsLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.lifecycle.viewmodel
         */
        public AndroidxLifecycleViewmodelLibraryAccessors getViewmodel() { return laccForAndroidxLifecycleViewmodelLibraryAccessors; }

    }

    public static class AndroidxLifecycleCommonLibraryAccessors extends SubDependencyFactory {

        public AndroidxLifecycleCommonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for java8 (androidx.lifecycle:lifecycle-common-java8)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getJava8() { return create("androidx-lifecycle-common-java8"); }

    }

    public static class AndroidxLifecycleLivedataLibraryAccessors extends SubDependencyFactory {

        public AndroidxLifecycleLivedataLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (androidx.lifecycle:lifecycle-livedata)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("androidx-lifecycle-livedata-core"); }

            /**
             * Creates a dependency provider for ktx (androidx.lifecycle:lifecycle-livedata-ktx)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx-lifecycle-livedata-ktx"); }

    }

    public static class AndroidxLifecycleReactivestreamsLibraryAccessors extends SubDependencyFactory {

        public AndroidxLifecycleReactivestreamsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ktx (androidx.lifecycle:lifecycle-reactivestreams-ktx)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx-lifecycle-reactivestreams-ktx"); }

    }

    public static class AndroidxLifecycleViewmodelLibraryAccessors extends SubDependencyFactory {

        public AndroidxLifecycleViewmodelLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ktx (androidx.lifecycle:lifecycle-viewmodel-ktx)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx-lifecycle-viewmodel-ktx"); }

            /**
             * Creates a dependency provider for savedstate (androidx.lifecycle:lifecycle-viewmodel-savedstate)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getSavedstate() { return create("androidx-lifecycle-viewmodel-savedstate"); }

    }

    public static class AndroidxNavigationLibraryAccessors extends SubDependencyFactory {
        private final AndroidxNavigationFragmentLibraryAccessors laccForAndroidxNavigationFragmentLibraryAccessors = new AndroidxNavigationFragmentLibraryAccessors(owner);
        private final AndroidxNavigationUiLibraryAccessors laccForAndroidxNavigationUiLibraryAccessors = new AndroidxNavigationUiLibraryAccessors(owner);

        public AndroidxNavigationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at androidx.navigation.fragment
         */
        public AndroidxNavigationFragmentLibraryAccessors getFragment() { return laccForAndroidxNavigationFragmentLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.navigation.ui
         */
        public AndroidxNavigationUiLibraryAccessors getUi() { return laccForAndroidxNavigationUiLibraryAccessors; }

    }

    public static class AndroidxNavigationFragmentLibraryAccessors extends SubDependencyFactory {

        public AndroidxNavigationFragmentLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ktx (androidx.navigation:navigation-fragment-ktx)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx-navigation-fragment-ktx"); }

    }

    public static class AndroidxNavigationUiLibraryAccessors extends SubDependencyFactory {

        public AndroidxNavigationUiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ktx (androidx.navigation:navigation-ui-ktx)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx-navigation-ui-ktx"); }

    }

    public static class AndroidxWindowLibraryAccessors extends SubDependencyFactory {

        public AndroidxWindowLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for java (androidx.window:window-java)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getJava() { return create("androidx-window-java"); }

            /**
             * Creates a dependency provider for window (androidx.window:window)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getWindow() { return create("androidx-window-window"); }

    }

    public static class ApacheLibraryAccessors extends SubDependencyFactory {
        private final ApacheHttpclientLibraryAccessors laccForApacheHttpclientLibraryAccessors = new ApacheHttpclientLibraryAccessors(owner);

        public ApacheLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at apache.httpclient
         */
        public ApacheHttpclientLibraryAccessors getHttpclient() { return laccForApacheHttpclientLibraryAccessors; }

    }

    public static class ApacheHttpclientLibraryAccessors extends SubDependencyFactory {

        public ApacheHttpclientLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (org.apache.httpcomponents:httpclient-android)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getAndroid() { return create("apache-httpclient-android"); }

    }

    public static class ConscryptLibraryAccessors extends SubDependencyFactory {

        public ConscryptLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (org.conscrypt:conscrypt-android)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getAndroid() { return create("conscrypt-android"); }

    }

    public static class EmilsjolanderLibraryAccessors extends SubDependencyFactory {

        public EmilsjolanderLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for stickylistheaders (se.emilsjolander:stickylistheaders)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getStickylistheaders() { return create("emilsjolander-stickylistheaders"); }

    }

    public static class ExoplayerLibraryAccessors extends SubDependencyFactory {
        private final ExoplayerExtensionLibraryAccessors laccForExoplayerExtensionLibraryAccessors = new ExoplayerExtensionLibraryAccessors(owner);

        public ExoplayerLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (com.google.android.exoplayer:exoplayer-core)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("exoplayer-core"); }

            /**
             * Creates a dependency provider for ui (com.google.android.exoplayer:exoplayer-ui)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getUi() { return create("exoplayer-ui"); }

        /**
         * Returns the group of libraries at exoplayer.extension
         */
        public ExoplayerExtensionLibraryAccessors getExtension() { return laccForExoplayerExtensionLibraryAccessors; }

    }

    public static class ExoplayerExtensionLibraryAccessors extends SubDependencyFactory {

        public ExoplayerExtensionLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for mediasession (com.google.android.exoplayer:extension-mediasession)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getMediasession() { return create("exoplayer-extension-mediasession"); }

    }

    public static class FirebaseLibraryAccessors extends SubDependencyFactory {

        public FirebaseLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for messaging (com.google.firebase:firebase-messaging)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getMessaging() { return create("firebase-messaging"); }

    }

    public static class GlideLibraryAccessors extends SubDependencyFactory {

        public GlideLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for compiler (com.github.bumptech.glide:compiler)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getCompiler() { return create("glide-compiler"); }

            /**
             * Creates a dependency provider for glide (com.github.bumptech.glide:glide)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getGlide() { return create("glide-glide"); }

    }

    public static class GoogleLibraryAccessors extends SubDependencyFactory {
        private final GoogleEzLibraryAccessors laccForGoogleEzLibraryAccessors = new GoogleEzLibraryAccessors(owner);
        private final GoogleGuavaLibraryAccessors laccForGoogleGuavaLibraryAccessors = new GoogleGuavaLibraryAccessors(owner);
        private final GooglePlayLibraryAccessors laccForGooglePlayLibraryAccessors = new GooglePlayLibraryAccessors(owner);
        private final GoogleProtobufLibraryAccessors laccForGoogleProtobufLibraryAccessors = new GoogleProtobufLibraryAccessors(owner);
        private final GoogleZxingLibraryAccessors laccForGoogleZxingLibraryAccessors = new GoogleZxingLibraryAccessors(owner);

        public GoogleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for jsr305 (com.google.code.findbugs:jsr305)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getJsr305() { return create("google-jsr305"); }

            /**
             * Creates a dependency provider for libphonenumber (com.googlecode.libphonenumber:libphonenumber)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getLibphonenumber() { return create("google-libphonenumber"); }

        /**
         * Returns the group of libraries at google.ez
         */
        public GoogleEzLibraryAccessors getEz() { return laccForGoogleEzLibraryAccessors; }

        /**
         * Returns the group of libraries at google.guava
         */
        public GoogleGuavaLibraryAccessors getGuava() { return laccForGoogleGuavaLibraryAccessors; }

        /**
         * Returns the group of libraries at google.play
         */
        public GooglePlayLibraryAccessors getPlay() { return laccForGooglePlayLibraryAccessors; }

        /**
         * Returns the group of libraries at google.protobuf
         */
        public GoogleProtobufLibraryAccessors getProtobuf() { return laccForGoogleProtobufLibraryAccessors; }

        /**
         * Returns the group of libraries at google.zxing
         */
        public GoogleZxingLibraryAccessors getZxing() { return laccForGoogleZxingLibraryAccessors; }

    }

    public static class GoogleEzLibraryAccessors extends SubDependencyFactory {

        public GoogleEzLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for vcard (com.googlecode.ez-vcard:ez-vcard)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getVcard() { return create("google-ez-vcard"); }

    }

    public static class GoogleGuavaLibraryAccessors extends SubDependencyFactory {

        public GoogleGuavaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (com.google.guava:guava)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getAndroid() { return create("google-guava-android"); }

    }

    public static class GooglePlayLibraryAccessors extends SubDependencyFactory {
        private final GooglePlayServicesLibraryAccessors laccForGooglePlayServicesLibraryAccessors = new GooglePlayServicesLibraryAccessors(owner);

        public GooglePlayLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at google.play.services
         */
        public GooglePlayServicesLibraryAccessors getServices() { return laccForGooglePlayServicesLibraryAccessors; }

    }

    public static class GooglePlayServicesLibraryAccessors extends SubDependencyFactory {

        public GooglePlayServicesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for auth (com.google.android.gms:play-services-auth)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getAuth() { return create("google-play-services-auth"); }

            /**
             * Creates a dependency provider for maps (com.google.android.gms:play-services-maps)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getMaps() { return create("google-play-services-maps"); }

            /**
             * Creates a dependency provider for wallet (com.google.android.gms:play-services-wallet)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getWallet() { return create("google-play-services-wallet"); }

    }

    public static class GoogleProtobufLibraryAccessors extends SubDependencyFactory {

        public GoogleProtobufLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for javalite (com.google.protobuf:protobuf-javalite)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getJavalite() { return create("google-protobuf-javalite"); }

    }

    public static class GoogleZxingLibraryAccessors extends SubDependencyFactory {
        private final GoogleZxingAndroidLibraryAccessors laccForGoogleZxingAndroidLibraryAccessors = new GoogleZxingAndroidLibraryAccessors(owner);

        public GoogleZxingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (com.google.zxing:core)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("google-zxing-core"); }

        /**
         * Returns the group of libraries at google.zxing.android
         */
        public GoogleZxingAndroidLibraryAccessors getAndroid() { return laccForGoogleZxingAndroidLibraryAccessors; }

    }

    public static class GoogleZxingAndroidLibraryAccessors extends SubDependencyFactory {

        public GoogleZxingAndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for integration (com.google.zxing:android-integration)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getIntegration() { return create("google-zxing-android-integration"); }

    }

    public static class GreenrobotLibraryAccessors extends SubDependencyFactory {

        public GreenrobotLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for eventbus (org.greenrobot:eventbus)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getEventbus() { return create("greenrobot-eventbus"); }

    }

    public static class JacksonLibraryAccessors extends SubDependencyFactory {
        private final JacksonModuleLibraryAccessors laccForJacksonModuleLibraryAccessors = new JacksonModuleLibraryAccessors(owner);

        public JacksonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (com.fasterxml.jackson.core:jackson-databind)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("jackson-core"); }

        /**
         * Returns the group of libraries at jackson.module
         */
        public JacksonModuleLibraryAccessors getModule() { return laccForJacksonModuleLibraryAccessors; }

    }

    public static class JacksonModuleLibraryAccessors extends SubDependencyFactory {

        public JacksonModuleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for kotlin (com.fasterxml.jackson.module:jackson-module-kotlin)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getKotlin() { return create("jackson-module-kotlin"); }

    }

    public static class JknackLibraryAccessors extends SubDependencyFactory {

        public JknackLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for handlebars (com.github.jknack:handlebars)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getHandlebars() { return create("jknack-handlebars"); }

    }

    public static class JpardogoLibraryAccessors extends SubDependencyFactory {

        public JpardogoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for materialtabstrip (com.jpardogo.materialtabstrip:library)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getMaterialtabstrip() { return create("jpardogo-materialtabstrip"); }

    }

    public static class KotlinLibraryAccessors extends SubDependencyFactory {
        private final KotlinStdlibLibraryAccessors laccForKotlinStdlibLibraryAccessors = new KotlinStdlibLibraryAccessors(owner);

        public KotlinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for reflect (org.jetbrains.kotlin:kotlin-reflect)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getReflect() { return create("kotlin-reflect"); }

        /**
         * Returns the group of libraries at kotlin.stdlib
         */
        public KotlinStdlibLibraryAccessors getStdlib() { return laccForKotlinStdlibLibraryAccessors; }

    }

    public static class KotlinStdlibLibraryAccessors extends SubDependencyFactory {

        public KotlinStdlibLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for jdk8 (org.jetbrains.kotlin:kotlin-stdlib-jdk8)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getJdk8() { return create("kotlin-stdlib-jdk8"); }

    }

    public static class LeolinLibraryAccessors extends SubDependencyFactory {

        public LeolinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for shortcutbadger (me.leolin:ShortcutBadger)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getShortcutbadger() { return create("leolin-shortcutbadger"); }

    }

    public static class LibsignalLibraryAccessors extends SubDependencyFactory {

        public LibsignalLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (org.signal:libsignal-android)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getAndroid() { return create("libsignal-android"); }

            /**
             * Creates a dependency provider for client (org.signal:libsignal-client)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getClient() { return create("libsignal-client"); }

    }

    public static class MaterialLibraryAccessors extends SubDependencyFactory {

        public MaterialLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for material (com.google.android.material:material)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getMaterial() { return create("material-material"); }

    }

    public static class MaterialishLibraryAccessors extends SubDependencyFactory {

        public MaterialishLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for progress (com.pnikosis:materialish-progress)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getProgress() { return create("materialish-progress"); }

    }

    public static class Mp4parserLibraryAccessors extends SubDependencyFactory {

        public Mp4parserLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for isoparser (org.mp4parser:isoparser)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getIsoparser() { return create("mp4parser-isoparser"); }

            /**
             * Creates a dependency provider for muxer (org.mp4parser:muxer)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getMuxer() { return create("mp4parser-muxer"); }

            /**
             * Creates a dependency provider for streaming (org.mp4parser:streaming)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getStreaming() { return create("mp4parser-streaming"); }

    }

    public static class NanohttpdLibraryAccessors extends SubDependencyFactory {

        public NanohttpdLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for webserver (org.nanohttpd:nanohttpd-webserver)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getWebserver() { return create("nanohttpd-webserver"); }

    }

    public static class Rxjava3LibraryAccessors extends SubDependencyFactory {

        public Rxjava3LibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for rxandroid (io.reactivex.rxjava3:rxandroid)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getRxandroid() { return create("rxjava3-rxandroid"); }

            /**
             * Creates a dependency provider for rxjava (io.reactivex.rxjava3:rxjava)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getRxjava() { return create("rxjava3-rxjava"); }

            /**
             * Creates a dependency provider for rxkotlin (io.reactivex.rxjava3:rxkotlin)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getRxkotlin() { return create("rxjava3-rxkotlin"); }

    }

    public static class SignalLibraryAccessors extends SubDependencyFactory {
        private final SignalAndroidLibraryAccessors laccForSignalAndroidLibraryAccessors = new SignalAndroidLibraryAccessors(owner);

        public SignalLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for aesgcmprovider (org.signal:aesgcmprovider)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getAesgcmprovider() { return create("signal-aesgcmprovider"); }

            /**
             * Creates a dependency provider for argon2 (org.signal:argon2)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getArgon2() { return create("signal-argon2"); }

            /**
             * Creates a dependency provider for ringrtc (org.signal:ringrtc-android)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getRingrtc() { return create("signal-ringrtc"); }

        /**
         * Returns the group of libraries at signal.android
         */
        public SignalAndroidLibraryAccessors getAndroid() { return laccForSignalAndroidLibraryAccessors; }

    }

    public static class SignalAndroidLibraryAccessors extends SubDependencyFactory {
        private final SignalAndroidDatabaseLibraryAccessors laccForSignalAndroidDatabaseLibraryAccessors = new SignalAndroidDatabaseLibraryAccessors(owner);

        public SignalAndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at signal.android.database
         */
        public SignalAndroidDatabaseLibraryAccessors getDatabase() { return laccForSignalAndroidDatabaseLibraryAccessors; }

    }

    public static class SignalAndroidDatabaseLibraryAccessors extends SubDependencyFactory {

        public SignalAndroidDatabaseLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for sqlcipher (org.signal:android-database-sqlcipher)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getSqlcipher() { return create("signal-android-database-sqlcipher"); }

    }

    public static class SquareLibraryAccessors extends SubDependencyFactory {

        public SquareLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for leakcanary (com.squareup.leakcanary:leakcanary-android)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getLeakcanary() { return create("square-leakcanary"); }

            /**
             * Creates a dependency provider for okhttp3 (com.squareup.okhttp3:okhttp)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getOkhttp3() { return create("square-okhttp3"); }

            /**
             * Creates a dependency provider for okio (com.squareup.okio:okio)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getOkio() { return create("square-okio"); }

    }

    public static class SubsamplingLibraryAccessors extends SubDependencyFactory {
        private final SubsamplingScaleLibraryAccessors laccForSubsamplingScaleLibraryAccessors = new SubsamplingScaleLibraryAccessors(owner);

        public SubsamplingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at subsampling.scale
         */
        public SubsamplingScaleLibraryAccessors getScale() { return laccForSubsamplingScaleLibraryAccessors; }

    }

    public static class SubsamplingScaleLibraryAccessors extends SubDependencyFactory {
        private final SubsamplingScaleImageLibraryAccessors laccForSubsamplingScaleImageLibraryAccessors = new SubsamplingScaleImageLibraryAccessors(owner);

        public SubsamplingScaleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at subsampling.scale.image
         */
        public SubsamplingScaleImageLibraryAccessors getImage() { return laccForSubsamplingScaleImageLibraryAccessors; }

    }

    public static class SubsamplingScaleImageLibraryAccessors extends SubDependencyFactory {

        public SubsamplingScaleImageLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for view (com.davemorrissey.labs:subsampling-scale-image-view)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getView() { return create("subsampling-scale-image-view"); }

    }

    public static class TimeLibraryAccessors extends SubDependencyFactory {
        private final TimeDurationLibraryAccessors laccForTimeDurationLibraryAccessors = new TimeDurationLibraryAccessors(owner);

        public TimeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at time.duration
         */
        public TimeDurationLibraryAccessors getDuration() { return laccForTimeDurationLibraryAccessors; }

    }

    public static class TimeDurationLibraryAccessors extends SubDependencyFactory {

        public TimeDurationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for picker (mobi.upod:time-duration-picker)
             * This dependency was declared in script '../dependencies.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getPicker() { return create("time-duration-picker"); }

    }

    public static class VersionAccessors extends VersionFactory {

        private final AndroidxVersionAccessors vaccForAndroidxVersionAccessors = new AndroidxVersionAccessors(providers, config);
        private final LibsignalVersionAccessors vaccForLibsignalVersionAccessors = new LibsignalVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: exoplayer (2.18.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in script '../dependencies.gradle'
             */
            public Provider<String> getExoplayer() { return getVersion("exoplayer"); }

            /**
             * Returns the version associated to this alias: glide (4.13.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in script '../dependencies.gradle'
             */
            public Provider<String> getGlide() { return getVersion("glide"); }

            /**
             * Returns the version associated to this alias: kotlin (1.6.21)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in script '../dependencies.gradle'
             */
            public Provider<String> getKotlin() { return getVersion("kotlin"); }

            /**
             * Returns the version associated to this alias: mp4parser (1.9.39)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in script '../dependencies.gradle'
             */
            public Provider<String> getMp4parser() { return getVersion("mp4parser"); }

        /**
         * Returns the group of versions at versions.androidx
         */
        public AndroidxVersionAccessors getAndroidx() { return vaccForAndroidxVersionAccessors; }

        /**
         * Returns the group of versions at versions.libsignal
         */
        public LibsignalVersionAccessors getLibsignal() { return vaccForLibsignalVersionAccessors; }

    }

    public static class AndroidxVersionAccessors extends VersionFactory {

        public AndroidxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: androidx-camera (1.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in script '../dependencies.gradle'
             */
            public Provider<String> getCamera() { return getVersion("androidx-camera"); }

            /**
             * Returns the version associated to this alias: androidx-fragment (1.5.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in script '../dependencies.gradle'
             */
            public Provider<String> getFragment() { return getVersion("androidx-fragment"); }

            /**
             * Returns the version associated to this alias: androidx-lifecycle (2.5.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in script '../dependencies.gradle'
             */
            public Provider<String> getLifecycle() { return getVersion("androidx-lifecycle"); }

            /**
             * Returns the version associated to this alias: androidx-navigation (2.5.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in script '../dependencies.gradle'
             */
            public Provider<String> getNavigation() { return getVersion("androidx-navigation"); }

            /**
             * Returns the version associated to this alias: androidx-window (1.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in script '../dependencies.gradle'
             */
            public Provider<String> getWindow() { return getVersion("androidx-window"); }

    }

    public static class LibsignalVersionAccessors extends VersionFactory {

        public LibsignalVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: libsignal-client (0.20.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in script '../dependencies.gradle'
             */
            public Provider<String> getClient() { return getVersion("libsignal-client"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a dependency bundle provider for exoplayer which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.google.android.exoplayer:exoplayer-core</li>
             *    <li>com.google.android.exoplayer:exoplayer-ui</li>
             *    <li>com.google.android.exoplayer:extension-mediasession</li>
             * </ul>
             * This bundle was declared in script '../dependencies.gradle'
             */
            public Provider<ExternalModuleDependencyBundle> getExoplayer() { return createBundle("exoplayer"); }

            /**
             * Creates a dependency bundle provider for mp4parser which is an aggregate for the following dependencies:
             * <ul>
             *    <li>org.mp4parser:isoparser</li>
             *    <li>org.mp4parser:streaming</li>
             *    <li>org.mp4parser:muxer</li>
             * </ul>
             * This bundle was declared in script '../dependencies.gradle'
             */
            public Provider<ExternalModuleDependencyBundle> getMp4parser() { return createBundle("mp4parser"); }

    }

}

plugins {
    alias(libs.plugins.cinecircle.android.application)
    alias(libs.plugins.cinecircle.hilt)
    alias(libs.plugins.secrets.gradle.plugin)
}

android {
    namespace = "com.koniukhov.cinecircle"

    defaultConfig {
        applicationId = "com.koniukhov.cinecircle"
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "com.koniukhov.cinecircle.HiltTestRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

secrets {
    propertiesFileName = "secrets.properties"
}

dependencies {

    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.domain)
    implementation(projects.core.design)
    implementation(projects.core.network)
    implementation(projects.core.ui)
    implementation(projects.feature.home)
    implementation(projects.feature.mediaDetails)
    implementation(projects.feature.collections)
    implementation(projects.feature.search)
    implementation(projects.feature.aiRecommendations)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.splashscreen)
    implementation(libs.timber)
    implementation(libs.bundles.coil)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.espresso.contrib)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.fragment.testing)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.mockk.android)
    kspAndroidTest(libs.hilt.android.compiler)
    debugImplementation(libs.androidx.fragment.testing.manifest)

    debugImplementation(libs.leakcanary.android)

    coreLibraryDesugaring(libs.android.desugarJdkLibs)
}
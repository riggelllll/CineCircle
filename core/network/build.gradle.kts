plugins {
    alias(libs.plugins.cinecircle.android.library)
    alias(libs.plugins.cinecircle.hilt)
}

android {
    namespace = "com.koniukhov.cinecircle.core.network"
}

dependencies{
    implementation(libs.bundles.retrofit)
    implementation(projects.core.common)
}
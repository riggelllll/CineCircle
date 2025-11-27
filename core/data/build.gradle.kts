plugins {
    alias(libs.plugins.cinecircle.android.library)
    alias(libs.plugins.cinecircle.hilt)
}

android {
    namespace = "com.koniukhov.cinecircle.core.data"
}

dependencies{
    implementation(projects.core.domain)
    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(projects.core.database)
    implementation(libs.bundles.retrofit)
}
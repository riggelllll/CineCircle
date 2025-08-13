plugins {
    alias(libs.plugins.cinecircle.android.feature)
    alias(libs.plugins.cinecircle.hilt)
}

android {
    namespace = "com.koniukhov.cinecircle.feature.home"
}

dependencies{
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.design)
    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    implementation(libs.bundles.coil)
}
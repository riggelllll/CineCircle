plugins {
    alias(libs.plugins.cinecircle.android.library)
}

android {
    namespace = "com.koniukhov.cinecircle.core.ui"
}

dependencies{
    implementation(libs.androidx.core.ktx)
    implementation(projects.core.design)
    implementation(projects.core.domain)
    implementation(projects.core.common)
    implementation(projects.core.network)
    implementation(libs.bundles.coil)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    implementation(libs.androidx.paging.runtime)
}
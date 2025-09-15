plugins {
    alias(libs.plugins.cinecircle.android.feature)
}

android {
    namespace = "com.koniukhov.cinecircle.feature.collections"
}

dependencies{
    implementation(projects.core.database)
    implementation(projects.core.design)
    implementation(libs.material)
}
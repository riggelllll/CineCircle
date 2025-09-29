plugins {
    alias(libs.plugins.cinecircle.android.library)
    alias(libs.plugins.cinecircle.hilt)
}

android {
    namespace = "com.koniukhov.cinecircle.core.domain"
}

dependencies{
    implementation(projects.core.common)
}
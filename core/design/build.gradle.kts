plugins {
    alias(libs.plugins.cinecircle.android.library)
}

android {
    namespace = "com.koniukhov.cinecircle.core.design"
}

dependencies{
    implementation(projects.core.common)
}
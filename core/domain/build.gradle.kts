plugins {
    alias(libs.plugins.cinecircle.android.library)
    alias(libs.plugins.cinecircle.hilt)
}

android {
    namespace = "com.koniukhov.cinecirclex.core.domain"
}

dependencies{
    implementation(projects.core.common)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}
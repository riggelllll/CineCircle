plugins {
    alias(libs.plugins.cinecircle.android.feature)
    alias(libs.plugins.cinecircle.hilt)
}

android {
    namespace = "com.koniukhov.cinecircle.feature.ai_recommendations"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.core.database)
    implementation(projects.core.design)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.jetbrains.bio.npy)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
}
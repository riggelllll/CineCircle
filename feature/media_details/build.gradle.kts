plugins {
    alias(libs.plugins.cinecircle.android.feature)
    alias(libs.plugins.cinecircle.hilt)
}

android {
    namespace = "com.koniukhov.cinecircle.feature.movie_details"
}

dependencies{
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.design)
    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    implementation(libs.bundles.coil)
    implementation(libs.skeletonlayout)
    implementation(libs.android.youtube.player)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
}
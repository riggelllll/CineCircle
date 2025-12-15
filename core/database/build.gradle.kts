plugins {
    alias(libs.plugins.cinecircle.android.library)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.cinecircle.hilt)
}

android {
    namespace = "com.koniukhov.cinecircle.core.database"
}

dependencies{
    implementation(projects.core.common)
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.robolectric)
}
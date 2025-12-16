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

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.room.testing)
}
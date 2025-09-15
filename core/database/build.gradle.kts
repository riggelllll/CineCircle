plugins {
    alias(libs.plugins.cinecircle.android.library)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.koniukhov.cinecircle.core.database"
}

dependencies{
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
}
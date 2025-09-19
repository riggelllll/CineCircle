plugins {
    alias(libs.plugins.cinecircle.android.library)
}

android {
    namespace = "com.koniukhov.cinecircle.core.common"
}

dependencies{
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
}
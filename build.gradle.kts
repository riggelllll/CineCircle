// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}

val secretsFile = rootProject.file("secrets.properties")
if (secretsFile.exists()) {
    val properties = java.util.Properties()
    properties.load(secretsFile.inputStream())
    properties.forEach { (key, value) ->
        extra[key.toString()] = value
    }
}
import com.android.build.gradle.LibraryExtension
import com.koniukhov.cinecircle.convention.configureKotlinAndroid
import com.koniukhov.cinecircle.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 36
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true

                packaging {
                    resources {
                        excludes += setOf(
                            "META-INF/LICENSE.md",
                            "META-INF/LICENSE-notice.md",
                            "META-INF/NOTICE.md"
                        )
                    }
                }

                buildFeatures {
                    buildConfig = true
                    viewBinding = true
                }

                val apiKey = target.rootProject.extra.get("API_KEY") as? String
                if (apiKey != null) {
                    defaultConfig {
                        buildConfigField("String", "API_KEY", "\"$apiKey\"")
                    }
                }
            }
            dependencies {
                "implementation"(libs.findLibrary("timber").get())
            }
        }

    }
}
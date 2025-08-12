import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<LibraryExtension> {
                compileSdk = 36
                defaultConfig {
                    minSdk = 24
                    targetSdk = 36
                }
                buildFeatures {
                    buildConfig = true
                }
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                val apiKey = target.rootProject.extra.get("API_KEY") as? String
                if (apiKey != null) {
                    defaultConfig {
                        buildConfigField("String", "API_KEY", "\"$apiKey\"")
                    }
                }
            }
            extensions.configure<KotlinAndroidProjectExtension> {
                jvmToolchain(17)
            }

        }

    }
}
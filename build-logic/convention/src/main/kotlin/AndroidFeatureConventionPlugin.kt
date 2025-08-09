import com.android.build.gradle.LibraryExtension
import com.koniukhov.cinecircle.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "cinecircle.android.library")

            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
                buildFeatures {
                    viewBinding = true
                }
            }
            extensions.configure<KotlinAndroidProjectExtension> {
                jvmToolchain(17)
            }

            dependencies {
                "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
                "implementation"(libs.findLibrary("androidx-navigation-fragment-ktx").get())
                "implementation"(libs.findLibrary("androidx-constraintlayout").get())
            }
        }
    }
}
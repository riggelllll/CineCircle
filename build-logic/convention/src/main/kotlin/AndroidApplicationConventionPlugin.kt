import com.android.build.api.dsl.ApplicationExtension
import com.koniukhov.cinecirclex.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 36
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

                compileOptions {
                    isCoreLibraryDesugaringEnabled = true
                }
            }
        }
    }
}
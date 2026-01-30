import com.koniukhov.cinecirclex.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.devtools.ksp")
            apply(plugin = "dagger.hilt.android.plugin")
            dependencies {
                "ksp"(libs.findLibrary("hilt.android.compiler").get())
                "implementation"(libs.findLibrary("hilt.android").get())
            }
        }
    }
}
package com.floosi.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FloosiAndroidLibraryComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply("floosi.android.library")
        target.plugins.apply("org.jetbrains.kotlin.plugin.compose")

        target.dependencies {
            add("implementation", target.libs().findLibrary("compose-bom").get())
            add("implementation", target.libs().findLibrary("compose-ui").get())
            add("implementation", target.libs().findLibrary("compose-ui-graphics").get())
            add("implementation", target.libs().findLibrary("compose-ui-tooling-preview").get())
            add("implementation", target.libs().findLibrary("compose-material3").get())
            add("implementation", target.libs().findLibrary("compose-material-icons").get())
            add("implementation", target.libs().findLibrary("compose-foundation").get())
            add("implementation", target.libs().findLibrary("compose-animation").get())
            add("implementation", target.libs().findLibrary("activity-compose").get())
            add("implementation", target.libs().findLibrary("lifecycle-runtime-compose").get())
            add("implementation", target.libs().findLibrary("lifecycle-viewmodel-compose").get())
            add("implementation", target.libs().findLibrary("coil-compose").get())
        }
    }
}

package com.floosi.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FloosiAndroidHiltPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply("com.google.dagger.hilt.android")
        target.plugins.apply("com.google.devtools.ksp")

        target.dependencies {
            add("implementation", target.libs().findLibrary("hilt-android").get())
            add("ksp", target.libs().findLibrary("hilt-compiler").get())
            add("implementation", target.libs().findLibrary("hilt-navigation-compose").get())
        }
    }
}

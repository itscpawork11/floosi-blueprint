package com.floosi.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FloosiAndroidRoomPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply("com.google.devtools.ksp")

        target.dependencies {
            add("implementation", target.libs().findLibrary("room-runtime").get())
            add("implementation", target.libs().findLibrary("room-ktx").get())
            add("ksp", target.libs().findLibrary("room-compiler").get())
        }
    }
}

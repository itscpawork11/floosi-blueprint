package com.floosi.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.repositories

class FloosiJvmLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply("org.jetbrains.kotlin.jvm")

        target.repositories {
            mavenCentral()
        }

        target.dependencies {
            add("implementation", target.libs().findLibrary("kotlin-stdlib").get())
            add("implementation", target.libs().findLibrary("coroutines-core").get())
        }
    }
}

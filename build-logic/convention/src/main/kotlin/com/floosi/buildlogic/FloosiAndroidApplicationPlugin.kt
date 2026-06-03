package com.floosi.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class FloosiAndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply("com.android.application")
        target.plugins.apply("org.jetbrains.kotlin.android")
        target.plugins.apply("org.jetbrains.kotlin.plugin.compose")

        target.extensions.configure<ApplicationExtension> {
            compileSdk = 35
            defaultConfig {
                minSdk = 26
                targetSdk = 35
                versionCode = 1
                versionName = "1.0.0"
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
            buildTypes {
                debug {
                    applicationIdSuffix = ".debug"
                    versionNameSuffix = "-debug"
                    isDebuggable = true
                }
                release {
                    isMinifyEnabled = true
                    isShrinkResources = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }
        }

        target.tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile::class.java).configureEach {
            kotlinOptions {
                jvmTarget = "17"
            }
        }

        target.dependencies {
            add("implementation", target.libs().findLibrary("kotlin-stdlib").get())
            add("implementation", target.libs().findLibrary("kotlinx-serialization-json").get())
            add("implementation", target.libs().findLibrary("kotlinx-datetime").get())
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
            add("implementation", target.libs().findLibrary("navigation-compose").get())
            add("implementation", target.libs().findLibrary("coil-compose").get())
            add("implementation", target.libs().findLibrary("profile-installer").get())
            add("debugImplementation", target.libs().findLibrary("compose-ui-tooling").get())
            add("debugImplementation", target.libs().findLibrary("compose-ui-test-manifest").get())
        }
    }
}

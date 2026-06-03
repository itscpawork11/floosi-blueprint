plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.agp)
    implementation(libs.kotlin.gradle)
    implementation(libs.ksp)
    implementation(libs.hilt.gradle)
    implementation(libs.room)
}

gradlePlugin {
    plugins {
        register("floosiAndroidApplication") {
            id = "floosi.android.application"
            implementationClass = "com.floosi.buildlogic.FloosiAndroidApplicationPlugin"
        }
        register("floosiAndroidLibrary") {
            id = "floosi.android.library"
            implementationClass = "com.floosi.buildlogic.FloosiAndroidLibraryPlugin"
        }
        register("floosiAndroidLibraryCompose") {
            id = "floosi.android.library.compose"
            implementationClass = "com.floosi.buildlogic.FloosiAndroidLibraryComposePlugin"
        }
        register("floosiAndroidHilt") {
            id = "floosi.android.hilt"
            implementationClass = "com.floosi.buildlogic.FloosiAndroidHiltPlugin"
        }
        register("floosiAndroidRoom") {
            id = "floosi.android.room"
            implementationClass = "com.floosi.buildlogic.FloosiAndroidRoomPlugin"
        }
        register("floosiJvmLibrary") {
            id = "floosi.jvm.library"
            implementationClass = "com.floosi.buildlogic.FloosiJvmLibraryPlugin"
        }
    }
}

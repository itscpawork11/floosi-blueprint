plugins {
    id("floosi.android.library.compose")
    id("floosi.android.hilt")
}

android {
    namespace = "com.floosi.feature.lock"
}

dependencies {
    implementation(project(":core:core-ui"))
    implementation(project(":core:core-security"))
    implementation(libs.navigation.compose)
}

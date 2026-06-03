plugins {
    id("floosi.android.library.compose")
    id("floosi.android.hilt")
}

android {
    namespace = "com.floosi.feature.lock"
}

dependencies {
    implementation(projects.core.coreUi)
    implementation(projects.core.coreSecurity)
    implementation(libs.navigation.compose)
}

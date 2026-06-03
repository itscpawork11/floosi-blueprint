plugins {
    id("floosi.android.library")
    id("floosi.android.hilt")
}

android {
    namespace = "com.floosi.security"
}

dependencies {
    implementation(projects.core.coreCommon)
    implementation(libs.security.crypto)
    implementation(libs.biometric)
}

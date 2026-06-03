plugins {
    id("floosi.android.library")
    id("floosi.android.hilt")
}

android {
    namespace = "com.floosi.security"
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(libs.security.crypto)
    implementation(libs.biometric)
}

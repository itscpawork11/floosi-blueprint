plugins {
    id("floosi.android.library.compose")
    id("floosi.android.hilt")
}

android {
    namespace = "com.floosi.feature.transactions"
}

dependencies {
    implementation(project(":core:core-ui"))
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-data"))
    implementation(libs.navigation.compose)
}

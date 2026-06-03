plugins {
    id("floosi.android.library.compose")
    id("floosi.android.hilt")
}

android {
    namespace = "com.floosi.feature.settings"
}

dependencies {
    implementation(projects.core.coreUi)
    implementation(projects.core.coreDomain)
    implementation(projects.core.coreData)
    implementation(libs.navigation.compose)
}

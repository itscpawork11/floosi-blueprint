plugins {
    id("floosi.android.library.compose")
}

android {
    namespace = "com.floosi.ui"
}

dependencies {
    implementation(project(":core:core-designsystem"))
    implementation(project(":core:core-common"))
}

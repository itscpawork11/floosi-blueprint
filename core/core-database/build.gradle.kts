plugins {
    id("floosi.android.library")
    id("floosi.android.room")
    id("floosi.android.hilt")
}

android {
    namespace = "com.floosi.database"
}

dependencies {
    implementation(project(":core:core-domain"))
    implementation(libs.compose.ui.graphics)
}

plugins {
    id("floosi.android.library.compose")
    id("floosi.android.room")
    id("floosi.android.hilt")
}

android {
    namespace = "com.floosi.database"
}

dependencies {
    implementation(project(":core:core-domain"))
}

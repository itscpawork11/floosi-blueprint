plugins {
    id("floosi.android.library")
    id("floosi.android.hilt")
}

android {
    namespace = "com.floosi.data"
}

dependencies {
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-database"))
    implementation("androidx.datastore:datastore-preferences:1.1.1")
}

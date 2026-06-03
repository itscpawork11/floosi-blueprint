plugins {
    id("floosi.jvm.library")
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(libs.kotlinx.datetime)
}

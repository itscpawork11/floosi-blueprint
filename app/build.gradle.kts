plugins {
    alias(libs.plugins.floosi.android.application)
    alias(libs.plugins.floosi.android.hilt)
}

android {
    namespace = "com.floosi.app"

    defaultConfig {
        applicationId = "com.floosi.app"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-designsystem"))
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-data")base)
    implementation(project(":core:core-data"))
    implementation(project(":core:core-ui"))
    implementation(project(":core:core-security"))
    implementation(project(":feature:feature-onboarding"))
    implementation(project(":feature:feature-lock"))
    implementation(project(":feature:feature-wallets"))
    implementation(project(":feature:feature-transactions"))
    implementation(project(":feature:feature-home"))
    implementation(project(":feature:feature-categories"))
    implementation(project(":feature:feature-search"))
    implementation(project(":feature:feature-settings"))
}

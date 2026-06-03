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
    implementation(projects.core.coreCommon)
    implementation(projects.core.coreDesignsystem)
    implementation(projects.core.coreDomain)
    implementation(projects.core.coreDatabase)
    implementation(projects.core.coreData)
    implementation(projects.core.coreUi)
    implementation(projects.core.coreSecurity)
    implementation(projects.feature.featureOnboarding)
    implementation(projects.feature.featureLock)
    implementation(projects.feature.featureWallets)
    implementation(projects.feature.featureTransactions)
    implementation(projects.feature.featureHome)
    implementation(projects.feature.featureCategories)
    implementation(projects.feature.featureSearch)
    implementation(projects.feature.featureSettings)
}

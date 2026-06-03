pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolution {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "floosi"

include(":app")
include(":core:core-common")
include(":core:core-designsystem")
include(":core:core-domain")
include(":core:core-database")
include(":core:core-data")
include(":core:core-ui")
include(":core:core-security")
include(":feature:feature-onboarding")
include(":feature:feature-lock")
include(":feature:feature-wallets")
include(":feature:feature-transactions")
include(":feature:feature-home")
include(":feature:feature-categories")
include(":feature:feature-search")
include(":feature:feature-settings")

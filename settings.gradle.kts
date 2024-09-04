pluginManagement {
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
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "androidx.navigation.safeargs") {
                useModule("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
            }
        }
    }
    plugins {
        id("androidx.navigation.safeargs.kotlin") version "2.7.7"
        id("com.google.devtools.ksp") version "2.0.20-1.0.24" apply false
//        id("org.jetbrains.kotlin.android") version "2.0.20"
//        id("org.jetbrains.kotlin.jvm") version "2.0.20"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Notes App"
include(":app")

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
//        maven(url = "https://maven.mappls.com/repository/mappls/")
        maven { url = uri("https://maven.mappls.com/repository/mappls/")}
//        maven { url = uri("https://maven.mapmyindia.com/repository/mapmyindia/") }
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
//        maven(url = "https://maven.mappls.com/repository/mappls/")
        maven { url = uri("https://maven.mappls.com/repository/mappls/")}
//        maven { url = uri("https://maven.mapmyindia.com/repository/mapmyindia/") }


    }
}

rootProject.name = "Millia Connect"
include(":app")
include(":feature:portal")
include(":feature:notice")
include(":feature:result")
include(":feature:attendance")
include(":core:data")
include(":core:domain")
include(":core:ui")
include(":core:common")
include(":core:navigation")
include(":core:network")
include(":core:notification")
include(":core:auth")
include(":feature:rent")
include(":core:analytics")
include(":core:firebase")
include(":core:config")
include(":core:location")

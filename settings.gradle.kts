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
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

//        // IronSource
//        maven(url = "https://android-sdk.is.com/")
//
//        // Pangle
//        maven(url = "https://artifact.bytedance.com/repository/pangle")
//
//        // Tapjoy
//        maven(url = "https://sdk.tapjoy.com/")
//
//        // Mintegral
//        maven(url = "https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
//
//        // Chartboost
//        maven(url = "https://cboost.jfrog.io/artifactory/chartboost-ads/")
//
//        // AppNext
//        maven(url = "https://dl.appnext.com/")
    }
}

rootProject.name = "Ninja"
include(":app")

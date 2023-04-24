pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "WorkControl"

include(":app")
include(":common:visitcontrol")
include(":common:visitcontrol:debug")
include(":shared:teacherservice")
include(":shared:database")
include(":shared:models")
include(":shared:studentservice")
include(":common:util")
include(":shared:notification")
include(":shared:util")

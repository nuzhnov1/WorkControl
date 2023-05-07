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
include(":common:util")
include(":common:visitcontrol")
include(":common:visitcontrol:debug")
include(":core:visitservice:util")
include(":core:visitservice:notification")
include(":core:visitservice:studentservice")
include(":core:visitservice:teacherservice")
include(":core:api")
include(":core:database")
include(":core:model")
include(":core:resources")
include(":core:room")
include(":core:preferences")
include(":core:work")
include(":core:util:coroutines")
include(":core:session")

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
include(":core:preferences")
include(":core:mapper")
include(":core:model")
include(":core:session")
include(":core:work")
include(":core:university")
include(":core:lesson")
include(":core:statistics")
include(":core:resources")
include(":core:util:coroutines")

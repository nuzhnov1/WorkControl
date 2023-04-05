plugins {
    kotlin("jvm") version "1.8.10"
    application
}

dependencies {
    implementation(project(":core:visitcontrol"))
    implementation("joda-time:joda-time:2.12.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_5
    targetCompatibility = JavaVersion.VERSION_1_5
}

application {
    mainClass.set("com.nuzhnov.workcontrol.core.controlservice.MainKt")
}

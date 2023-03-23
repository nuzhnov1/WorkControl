plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(project(":core:controlservice"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClass.set("com.nuzhnov.workcontrol.core.controlservice.MainKt")
}

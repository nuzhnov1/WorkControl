plugins {
    kotlin("jvm") version "1.8.10"
    `java-library`
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

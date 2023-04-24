plugins {
    kotlin("jvm")

    `java-library`
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

kotlin {
    java.sourceCompatibility = JavaVersion.VERSION_1_5
    java.targetCompatibility = JavaVersion.VERSION_1_5
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_5
    targetCompatibility = JavaVersion.VERSION_1_5
}

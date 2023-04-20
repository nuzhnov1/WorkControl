plugins {
    kotlin("jvm")
    `java-library`
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.soywiz.korlibs.klock:klock-jvm:2.2.0")
}

kotlin {
    java.sourceCompatibility = JavaVersion.VERSION_1_5
    java.targetCompatibility = JavaVersion.VERSION_1_5
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_5
    targetCompatibility = JavaVersion.VERSION_1_5
}
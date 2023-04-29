plugins {
    kotlin("jvm")
    kotlin("kapt")

    `java-library`
}

dependencies {
    implementation(project(":core:models"))
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.moshi:moshi:1.14.0")
    implementation("com.soywiz.korlibs.klock:klock-jvm:2.2.0")

    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")
}

kotlin {
    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_1_8
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

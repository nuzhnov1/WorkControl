plugins {
    kotlin("kapt")
    kotlin("android")

    id("com.android.library")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.nuzhnov.workcontrol.core.preferences"
    compileSdk = 33

    defaultConfig {
        minSdk = 14

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(name = "proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlin {
        java.sourceCompatibility = JavaVersion.VERSION_1_8
        java.targetCompatibility = JavaVersion.VERSION_1_8
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:util:coroutines"))
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.google.dagger:hilt-android:2.44")
    implementation("com.squareup.moshi:moshi:1.14.0")

    kapt("com.google.dagger:hilt-android-compiler:2.44")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")
}

kapt {
    correctErrorTypes = true
}

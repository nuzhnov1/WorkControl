plugins {
    kotlin("kapt")
    kotlin("android")

    id("com.android.library")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.nuzhnov.workcontrol.core.visitservice.studentservice"
    compileSdk = 33

    defaultConfig {
        minSdk = 16

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
    implementation(project(":core:visitservice:notification"))
    implementation(project(":core:visitservice:util"))
    implementation(project(":core:model"))
    implementation(project(":core:util:coroutines"))
    implementation(project(":common:visitcontrol"))
    implementation(project(":common:util"))
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("com.google.dagger:hilt-android:2.44")

    kapt("com.google.dagger:hilt-android-compiler:2.44")
}

kapt {
    correctErrorTypes = true
}

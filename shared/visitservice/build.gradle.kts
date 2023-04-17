plugins {
    kotlin("kapt")
    kotlin("android")

    id("com.android.library")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.nuzhnov.workcontrol.shared.visitservice"
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
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.room:room-runtime:2.5.1")
    implementation("androidx.room:room-ktx:2.5.1")
    implementation("com.google.dagger:hilt-android:2.44")
    implementation("joda-time:joda-time:2.12.4")
    implementation(project(":core:visitcontrol"))

    kapt("com.google.dagger:hilt-android-compiler:2.44")
    kapt("androidx.room:room-compiler:2.5.1")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
}

kapt {
    correctErrorTypes = true
}

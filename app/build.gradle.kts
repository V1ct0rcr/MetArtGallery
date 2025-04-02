plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "de.appsfactory.lecture.network"
    compileSdk = 35

    defaultConfig {
        applicationId = "de.appsfactory.lecture.counter"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlin {
        jvmToolchain(jdkVersion = 21)
    }

    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.coil.compose)

    implementation(libs.koin.androidx.compose.v350)
    implementation(libs.koin.android.v350)


    implementation(libs.koin.android.v353)
    implementation(libs.koin.androidx.compose.v353)

    // Moshi core
    implementation(libs.moshi.v1150)

// Adapter pentru Moshi cu Retrofit
    implementation(libs.converter.moshi.v290)

// Codegen pentru Moshi (folosit pentru @JsonClass)
    ksp(libs.moshi.kotlin.codegen.v1150)



    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    implementation(libs.navigation)
    implementation(libs.kotlinx.serialization.core)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.moshiConverter)
    implementation(libs.retrofit.loggingInterceptor)



    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}
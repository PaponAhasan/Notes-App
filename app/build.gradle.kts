plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hiltAndroid)
    //alias(libs.plugins.devtoolsKsp)
    //alias(libs.plugins.kotlinKapt)
    id("kotlin-kapt")
    //kotlin("kapt")
    id("com.starter.easylauncher") version "6.2.0"
}

android {
    namespace = "com.example.notesappadvance"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.notesappadvance"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    //build types
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        flavorDimensions += "version"
        productFlavors {
            create("production") {
                dimension = "version"
                buildConfigField("String", "BASE_URL", "\"https://api.escuelajs.co/api/v1\"")
            }
            create("development") {
                dimension = "version"
                resValue("string", "api_base_url", "https://api.escuelajs.co/api/v1")
                buildConfigField("String", "BASE_URL", "\"https://api.escuelajs.co/api/v1\"")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
    }

    kapt {
        correctErrorTypes = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    kapt(libs.androidx.lifecycle.compiler)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.timber)

    implementation(libs.androidx.core.splashscreen)

    implementation("com.github.ybq:Android-SpinKit:1.4.0")

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core)

    implementation(libs.glide)
    kapt(libs.compiler)
}

easylauncher {
   defaultFlavorNaming(true)

    buildTypes {
        register("debug") {
           filters(customRibbon(label = "Debug", ribbonColor = "#ff4715"))
        }
        register("release") {
            enable(false)
        }
    }
}

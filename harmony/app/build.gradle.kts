val appCompatVersion = "1.3.0"  // Definiendo la versión de appcompat

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.example.v2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.v2"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["redirectSchemeName"] = "com.miaplicacion"
        manifestPlaceholders["redirectHostName"] = "callback"
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(files("../spotify-app-remote-release-0.8.0.aar"))
    //implementation(files("../spotify-auth-release-2.1.0.aar"))
    //implementation(files("../spotify-auth-store-release-2.1.0.aar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.spotify.android:auth:1.2.5")

    // Dependencias adicionales para tu aplicación
    implementation("androidx.browser:browser:1.0.0")
    implementation("androidx.appcompat:appcompat:$appCompatVersion")
}
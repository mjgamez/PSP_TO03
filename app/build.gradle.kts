plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.mariajosegamez.psp_to03"
    compileSdk {
        version = release(36)
    }

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.mariajosegamez.psp_to03"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // gson
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // okhttp3
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.github.bumptech.glide:glide:4.16.0") // Para la imagen
    implementation("org.json:json:20231013")

}
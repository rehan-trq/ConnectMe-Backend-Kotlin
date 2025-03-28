plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.arshman_rehan.i222427_i220965"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.arshman_rehan.i222427_i220965"
        minSdk = 24
        targetSdk = 35
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

    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Add CameraX dependencies
    implementation(files("libs/camera-core-1.4.1.aar"))
    implementation(files("libs/camera-camera2-1.4.1.aar"))
    implementation(files("libs/camera-lifecycle-1.4.1.aar"))
    implementation(files("libs/camera-view-1.4.1.aar"))

    // Add ExifInterface dependency to fix the crash
    implementation (files("libs/exifinterface-1.3.7.aar"))

}
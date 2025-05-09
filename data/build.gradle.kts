import java.util.Properties

plugins {
    kotlin("kapt")
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)

    id("com.google.gms.google-services")
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.factory.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

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
            buildConfigField("String", "API_URL_ALL", properties.getProperty("API_URL_ALL"))
            buildConfigField("String", "API_URL_GYEONGGI", properties.getProperty("API_URL_GYEONGGI"))
            buildConfigField("String", "API_KEY_ALL", properties.getProperty("API_KEY_ALL"))
            buildConfigField("String", "API_KEY_GYEONGGI", properties.getProperty("API_KEY_GYEONGGI"))
        }
        debug {
            buildConfigField("String", "API_URL_ALL", properties.getProperty("API_URL_ALL"))
            buildConfigField("String", "API_URL_GYEONGGI", properties.getProperty("API_URL_GYEONGGI"))
            buildConfigField("String", "API_KEY_ALL", properties.getProperty("API_KEY_ALL"))
            buildConfigField("String", "API_KEY_GYEONGGI", properties.getProperty("API_KEY_GYEONGGI"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        buildConfig = true
    }
}

dependencies {
    implementation(projects.domain)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.timber)

    implementation(libs.hilt)
    kapt (libs.dagger.hilt.compiler)
    kapt (libs.androidx.hilt.compiler)

    implementation(libs.room.runtime)
    kapt (libs.room.compiler)
    implementation (libs.room.ktx) // Room Flow

    implementation (libs.retrofit)
    implementation (libs.retrofit.converter.moshi)
    implementation (libs.converter.gson)

    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)

    implementation(libs.androidx.dataStore.preferences)

    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth")
}
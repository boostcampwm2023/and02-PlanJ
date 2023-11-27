import java.util.Properties

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.plugin.hilt.android)
    kotlin("kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}


// 선언 및 키값을 불러오기
val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.boostcamp.planj"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.boostcamp.planj"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        android.buildFeatures.buildConfig = true

        // 프로젝트에서 사용
        // 타입 - 키 - 값
        buildConfigField("String", "NAVER_API_KEY", properties["naverKey"] as String)
        buildConfigField("String", "KAKAO_BASE_URL", properties["kakaoBaseUrl"] as String)
        buildConfigField("String", "KAKAO_REST_API", properties["kakaoRestApi"] as String)
        buildConfigField("String", "DATA_STORE_NAME", properties["dateStoreName"] as String)
        buildConfigField("String", "BASE_URL", properties["baseUrl"] as String)
        buildConfigField("String", "USER", properties["user"] as String)


        manifestPlaceholders["NAVER_API_KEY"] = properties["naverKey"] as String
        manifestPlaceholders["KAKAO_BASE_URL"] = properties["kakaoBaseUrl"] as String
        manifestPlaceholders["KAKAO_REST_API"] = properties["kakaoRestApi"] as String
        manifestPlaceholders["DATA_STORE_NAME"] = properties["dateStoreName"] as String
        manifestPlaceholders["BASE_URL"] = properties["baseUrl"] as String
        manifestPlaceholders["USER"] = properties["user"] as String
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    viewBinding {
        enable = true
    }
    dataBinding {
        enable = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.hilt.android)
    kapt (libs.hilt.compiler)

    //retrofit
    implementation (libs.retrofit)
    implementation(libs.converter.gson)

    //okhttp3
    implementation(libs.okhttp)
    // define any required OkHttp artifacts without version
    implementation(libs.logging.interceptor)

    //room
    implementation (libs.androidx.room.runtime)
    annotationProcessor (libs.androidx.room.compiler)
    kapt (libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)


    //jetpack navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.kotlinx.serialization.json)

    //네이버 지도
    implementation(libs.map.sdk)

    //dateStore
    implementation(libs.androidx.datastore.preferences)
}
import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-kapt")
    id("com.google.devtools.ksp") version "2.1.10-1.0.29"
    alias(libs.plugins.buildConfig).apply(false)
}

android {
    namespace = "com.example.appning"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.aptoideapp"
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.4"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.work.runtime.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.rxjava2)
    implementation(libs.androidx.room.rxjava3)
    implementation(libs.androidx.room.guava)
    testImplementation(libs.androidx.room.testing)
    implementation(libs.androidx.room.paging)
    implementation(libs.sqldelight)

    // Koin for Dependency Injection
    implementation (libs.insert.koin.koin.android)
    implementation (libs.koin.androidx.compose)
    implementation (libs.koin.androidx.navigation)

    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.ktor.client.core.v313)
    implementation(libs.ktor.client.okhttp.v313)
    implementation(libs.ktor.client.content.negotiation.v313)
    implementation(libs.ktor.serialization.kotlinx.json.v313)

    implementation (libs.androidx.core.ktx.v1131)
    implementation (libs.androidx.core.ktx)
    testImplementation(libs.ktor.client.mock.v313)
    implementation(libs.kotlinx.serialization.json.v163)
    // For tests, include client engine mock and ktor-io
    testImplementation (libs.ktor.utils)
    implementation (libs.androidx.navigation.compose)
    implementation(libs.coil.compose)

    // Ktor client
    implementation(libs.gson)

    // Testing
    testImplementation ("io.mockk:mockk:1.13.7")
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("app.cash.turbine:turbine:1.0.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation ("io.mockk:mockk:1.13.10")
    testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("org.robolectric:robolectric:4.11.1")
    testImplementation(libs.io.mockk.mockk)
    testImplementation(libs.slf4j.nop)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
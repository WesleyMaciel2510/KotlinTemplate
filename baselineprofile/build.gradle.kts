plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.template.app.baselineprofile"
    compileSdk { version = release(37) }

    defaultConfig {
        minSdk = 26
        targetSdk = 37
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    
    // Configure target app for baseline profile
    targetProjectPath = ":app"
}

dependencies {
    // Benchmark
    implementation(libs.androidx.benchmark.macro.junit4)
    
    // AndroidX Test
    implementation("androidx.test:core:1.5.0")
    implementation("androidx.test:rules:1.5.0")
    implementation("androidx.test.ext:junit:1.1.5")
    implementation(libs.junit)
    
    // Target app
    implementation(project(":app"))
}
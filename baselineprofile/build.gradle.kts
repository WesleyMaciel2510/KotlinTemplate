plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.template.app.baselineprofile"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        targetSdk = 36
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(21)
    }
}

dependencies {
    // Baseline Profile
    implementation(libs.androidx.baselineprofile)

    // Benchmark
    implementation(libs.androidx.benchmark.macro.junit4)

    // Target app
    implementation(project(":app"))
}
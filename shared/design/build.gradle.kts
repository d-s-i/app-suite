plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "app.suite.design"
    compileSdk = 35
    defaultConfig { minSdk = 26 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    lint { abortOnError = true }
}
kotlin { jvmToolchain(17) }

// The existing Compose version, not a new UI dependency. Color is part of our public API.
dependencies {
    api("androidx.compose.ui:ui:1.7.8")
}

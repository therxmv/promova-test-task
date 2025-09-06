plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.therxmv.base.date"
    compileSdk = libs.versions.project.targetSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.project.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    api(libs.kotlin.datetime)
}
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.therxmv.featuremovies.data"
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
    implementation(project(":feature-movies:domain"))

    implementation(libs.bundles.room)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.koin)

    testImplementation(libs.bundles.test.unit)
}
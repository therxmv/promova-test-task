plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.therxmv.featuremovies.ui"
    compileSdk = libs.versions.project.targetSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.project.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":feature-movies:domain"))
    implementation(project(":base:ui"))
    implementation(project(":base:date"))
    implementation(project(":base:navigation"))

    implementation(libs.bundles.koin)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.paging.compose)
    implementation(libs.bundles.coil)

    implementation(libs.bundles.viewModel)
}
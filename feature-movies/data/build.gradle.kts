import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.therxmv.featuremovies.data"
    compileSdk = libs.versions.project.targetSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.project.minSdk.get().toInt()

        defineLocalProperties {
            buildConfigField("String", "TMDB_TOKEN", "\"${it.getProperty("TMDB_TOKEN")}\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":feature-movies:domain"))
    implementation(project(":base:network"))

    implementation(libs.bundles.koin)

    implementation(libs.bundles.room)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.koin)

    testImplementation(libs.bundles.test.unit)
}

fun defineLocalProperties(action: (Properties) -> Unit) {
    val localFile = File(rootProject.rootDir, "local.properties")
        .takeIf { it.exists() && it.isFile } ?: return

    localFile.inputStream().use {
        action(
            Properties().apply { load(it) }
        )
    }
}
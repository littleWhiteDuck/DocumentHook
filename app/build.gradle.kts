import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val keyFile = rootProject.file("sign.properties")
val prop = Properties()
prop.load(FileInputStream(keyFile))

android {
    namespace = "me.documenthook"
    compileSdk = 33

    defaultConfig {
        applicationId = "me.documenthook"
        minSdk = 33
        targetSdk = 33
        versionCode = 3
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("keyStore") {
            keyAlias = prop.getProperty("alias")
            keyPassword = prop.getProperty("keyPassword")
            storeFile = File(prop.getProperty("file"))
            storePassword = prop.getProperty("password")
            enableV3Signing = true
        }
    }

    buildTypes {
        val signConfig = signingConfigs.getByName("keyStore")
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signConfig
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        getByName("debug") {
            signingConfig = signConfig
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    packagingOptions.resources.excludes += setOf(
        "META-INF/**", "okhttp3/**", "kotlin/**", "org/**", "**.properties", "**.bin", "**.json"
    )

    dependenciesInfo.includeInApk = false
    flavorDimensions += listOf("default")
    productFlavors {
        create("SimpleHook") {
            dimension = "default"
        }
        create("Normal") {
            dimension = "default"
        }
    }

}

dependencies {

/*    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")*/
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    // xposed
    compileOnly("de.robv.android.xposed:api:82")
}
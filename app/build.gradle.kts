import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-parcelize")
    id("com.apollographql.apollo3") version "3.8.2"
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply { load(FileInputStream(keystorePropertiesFile)) }

@Suppress("UnstableApiUsage")
android {
    namespace = "eamato.funn.r6companion"
    compileSdk = 34

    defaultConfig {
        applicationId = "eamato.funn.r6companion"
        minSdk = 24
        targetSdk = 34
        multiDexEnabled = true
        versionCode = 24
        versionName = "24"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resourceConfigurations.addAll(listOf("en", "ru", "es", "pt", "de", "fr", "it"))
    }

    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"] ?: "")
            keyAlias = keystoreProperties["keyAlias"] as String?
            keyPassword = keystoreProperties["keyPassword"] as String?
            storePassword = keystoreProperties["storePassword"] as String?
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "R6Companion(dev)")
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }

        create("staging") {
            initWith(getByName("release"))
            isDebuggable = true
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            resValue("string", "app_name", "R6Companion(staging)")
        }

        create("benchmark") {
            initWith(getByName("release"))
            matchingFallbacks.add("release")
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            proguardFiles("benchmark-rules.pro")
            isDebuggable = false
            isShrinkResources = false
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    apollo {
        generateKotlinModels.set(true)
        packageNamesFromFilePaths()
        service("service") {
            outputDirConnection {
                connectToKotlinSourceSet("main")
            }
            packageName.set("eamato.funn.r6companion")
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    /* AndroidX dependencies */
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.datastore:datastore-preferences-core:1.0.0")
    implementation("androidx.appcompat:appcompat-resources:1.6.1")
    implementation("androidx.profileinstaller:profileinstaller:1.3.1")

    /* Google dependencies */
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-compiler:2.44.2")

    /* Retrofit dependencies */
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

    /* Glide dependencies */
    implementation("com.github.bumptech.glide:annotations:4.15.1")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.15.1")

    /* Apollo dependencies */
    implementation("com.apollographql.apollo3:apollo-runtime:3.8.2")

    /* Firebase dependencies */
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-crashlytics:18.6.0")
    implementation("com.google.firebase:firebase-analytics:21.5.0")
    implementation("com.google.firebase:firebase-config-ktx:21.6.0")
    implementation("com.google.firebase:firebase-messaging:23.4.0")
    implementation("com.google.firebase:firebase-inappmessaging-display:20.4.0")
    implementation("com.google.firebase:firebase-ads:22.6.0")
    implementation("com.google.firebase:firebase-dynamic-links-ktx:21.2.0")

    /* Test dependencies */
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    /* Debug */
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")
}
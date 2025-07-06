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
    id("com.apollographql.apollo3") version "3.8.6"
    id("com.google.protobuf") version "0.9.4"
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply { load(FileInputStream(keystorePropertiesFile)) }

android {
    namespace = "eamato.funn.r6companion"
    compileSdk = 35

    defaultConfig {
        applicationId = "eamato.funn.r6companion"
        minSdk = 24
        targetSdk = 35
        multiDexEnabled = true
        versionCode = 28
        versionName = "28"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    androidResources {
        @Suppress("UnstableApiUsage") // TODO remove when becomes stable
        localeFilters += listOf("en", "ru", "es", "pt", "de", "fr", "it")
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

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.4"
    }

    generateProtoTasks {
        all().forEach { generateProtoTask ->
            generateProtoTask.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {

    /* AndroidX dependencies */
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.paging:paging-runtime-ktx:3.3.6")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.datastore:datastore:1.1.7")
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("androidx.profileinstaller:profileinstaller:1.4.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    /* Google dependencies */
    implementation("com.google.android.gms:play-services-ads:24.4.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("com.google.protobuf:protobuf-javalite:3.25.5")

    /* Retrofit dependencies */
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

    /* Glide dependencies */
    implementation("com.github.bumptech.glide:annotations:4.15.1")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.15.1")
    //noinspection KaptUsageInsteadOfKsp
    kapt("com.github.bumptech.glide:compiler:4.15.1")

    /* Apollo dependencies */
    implementation("com.apollographql.apollo3:apollo-runtime:3.8.6")

    /* Firebase dependencies */
    implementation("com.google.firebase:firebase-crashlytics:19.4.4")
    implementation("com.google.firebase:firebase-config-ktx:22.1.2")
    implementation("com.google.firebase:firebase-messaging:24.1.2")
    implementation("com.google.firebase:firebase-inappmessaging-display:21.0.2")
    implementation("com.google.firebase:firebase-dynamic-links-ktx:22.1.0")
    implementation("com.google.firebase:firebase-analytics-ktx:22.5.0")


    /* Test dependencies */
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    /* Debug */
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")
}
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")
    id("dagger.hilt.android.plugin")
    id ("kotlin-parcelize")

}

android {
    compileSdk = 31
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.d3if3150.catatin"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = true

    }


    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    // Architectural Components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")

    // Room
    implementation("androidx.room:room-runtime:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")
    kapt("org.xerial:sqlite-jdbc:3.36.0.3")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.3.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.2-native-mt")

    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

    // activity & fragment ktx
    implementation("androidx.fragment:fragment-ktx:1.3.6")
    implementation("androidx.activity:activity-ktx:1.4.0-rc01")
    implementation("androidx.appcompat:appcompat:1.4.0-beta01")

    // Navigation Components
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")

    // material design
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // Preference DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Lottie Animation Library
    implementation("com.airbnb.android:lottie:4.2.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.39.1")
    kapt("com.google.dagger:hilt-android-compiler:2.39.1")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")

    // Glide
    implementation ("com.github.bumptech.glide:glide:4.15.1")

    // Moshi
    implementation ("com.squareup.moshi:moshi-kotlin:1.9.3")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")

    // GSon
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // WorkInBackground
//    implementation ("androidx.work:work-runtime-ktx:2.7.1")
}

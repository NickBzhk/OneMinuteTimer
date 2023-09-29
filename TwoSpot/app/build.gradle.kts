plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.bzhk.twospot"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bzhk.twospot"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Open Street Map API
//    implementation("com.github.MKergall:osmbonuspack:6.9.0")
    implementation("org.osmdroid:osmdroid-android:6.1.17")

    // Location
    implementation("com.google.android.gms:play-services-location:21.0.1")

    //  View Model set
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-ktx:1.7.2")

    //  Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.2")

    //  Koin
    implementation("io.insert-koin:koin-android:3.4.3")

    //  Recycler View
    implementation("androidx.recyclerview:recyclerview:1.3.1")

    //  Coil
    implementation("io.coil-kt:coil:2.4.0")

    //  Retrofit 2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //  Gson
    implementation("com.google.code.gson:gson:2.10.1")
}

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.imager.edit_it"
    compileSdk = 34



    defaultConfig {
        applicationId = "com.imager.edit_it"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

    }



    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }

}


    dependencies {
        implementation("io.github.afreakyelf:Pdf-Viewer:2.1.1")
        implementation("com.squareup.okhttp3:okhttp:4.9.1")
        implementation("com.airbnb.android:lottie:6.4.0")
        implementation("com.squareup.picasso:picasso:2.71828")
        implementation("androidx.cardview:cardview:1.0.0")
        implementation("androidx.security:security-crypto:1.1.0-alpha03")
        implementation("com.github.bumptech.glide:glide:4.12.0")
        implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
        implementation("com.google.firebase:firebase-analytics")
        implementation("com.github.skydoves:colorpickerview:2.3.0")
        implementation("com.itextpdf:itext7-core:7.1.16")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.10.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
        implementation("androidx.navigation:navigation-fragment:2.7.5")
        implementation("androidx.navigation:navigation-ui:2.7.5")
        implementation("androidx.preference:preference:1.2.1")
        implementation("androidx.core:core-ktx:+")
        implementation("androidx.core:core-ktx:+")
        implementation("androidx.core:core-ktx:+")
        implementation("com.google.android.gms:play-services-base:18.3.0")
        implementation("com.google.firebase:firebase-auth:22.3.1")
        implementation("com.google.firebase:firebase-database:20.3.1")
        implementation("com.google.firebase:firebase-storage:20.3.0")
        implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    }

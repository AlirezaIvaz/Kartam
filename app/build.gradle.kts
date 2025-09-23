import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
}

val isBundleBuild = gradle.startParameter.taskNames.any { it.contains("bundle", ignoreCase = true) }

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

android {
    namespace = "ir.alirezaivaz.kartam"
    compileSdk = 36

    defaultConfig {
        applicationId = "ir.alirezaivaz.kartam"
        minSdk = 23
        targetSdk = 36
        versionCode = 140301
        versionName = "1.4.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }
    splits {
        abi {
            isEnable = !isBundleBuild
            isUniversalApk = true
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }
    applicationVariants.all {
        val variant = this
        val versionCodes = mapOf(
            "all" to 0,
            "arm64-v8a" to 1,
            "armeabi-v7a" to 2,
            "x86" to 3,
            "x86_64" to 4
        )
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.ApkVariantOutputImpl }
            .forEach { output ->
                val abi = output.getFilter("ABI") ?: "all"
                output.outputFileName = "Kartam_${variant.versionName}_${abi}.apk"
                if (versionCodes.containsKey(abi)) {
                    val abiCode = versionCodes[abi]!! * 10
                    val versionCode = variant.versionCode * 100
                    output.versionCodeOverride = abiCode.plus(versionCode)
                }
            }
    }
    flavorDimensions += "default"
    productFlavors {
        create("telegram") {
            dimension = "default"
            versionNameSuffix = "-TG"
            buildConfigField("String", "RATE_URL", "\"https://t.me/AlirezaIvaz\"")
            buildConfigField("String", "APPS_URL", "\"https://alirezaivaz.ir\"")
        }
        create("cafebazaar") {
            dimension = "default"
            versionNameSuffix = "-CB"
            buildConfigField("String", "RATE_URL", "\"bazaar://details?id=${defaultConfig.applicationId}\"")
            buildConfigField("String", "APPS_URL", "\"bazaar://collection?slug=by_author&aid=alirezaivaz\"")
        }
        create("myket") {
            dimension = "default"
            versionNameSuffix = "-MK"
            buildConfigField("String", "RATE_URL", "\"myket://comment?id=${defaultConfig.applicationId}\"")
            buildConfigField("String", "APPS_URL", "\"myket://developer/${defaultConfig.applicationId}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.biometric.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.androidx.material)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.dff)
    implementation(libs.google.gson)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.reorderable)
    implementation(libs.jalalicalendar)
    implementation(libs.sonner)
    implementation(libs.multiplatform.settings.noargs)
    implementation(project(":composedatepicker"))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

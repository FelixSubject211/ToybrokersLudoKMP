import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinPluginSerialization)
    alias(libs.plugins.kotlinx.rpc)
    alias(libs.plugins.mokkery)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosArm64()
    iosSimulatorArm64()
    iosX64()

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.rpc.core)
            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    namespace = "com.toybrokers.ludo.shared"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

buildkonfig {
    packageName = "com.toybrokers.ludo"

    defaultConfigs {
        buildConfigField(FieldSpec.Type.INT, "blueLastIndexBeforeEnd", 9.toString())
        buildConfigField(FieldSpec.Type.INT, "blueStart", 10.toString())
        buildConfigField(FieldSpec.Type.INT, "endMaxNumber", 3.toString())
        buildConfigField(FieldSpec.Type.INT, "greenLastIndexBeforeEnd", 19.toString())
        buildConfigField(FieldSpec.Type.INT, "greenStart", 20.toString())
        buildConfigField(FieldSpec.Type.INT, "maxDiceNumber", 6.toString())
        buildConfigField(FieldSpec.Type.INT, "redLastIndexBeforeEnd", 39.toString())
        buildConfigField(FieldSpec.Type.INT, "redStart", 0.toString())
        buildConfigField(FieldSpec.Type.INT, "remainingAttempts", 2.toString())
        buildConfigField(FieldSpec.Type.INT, "trackMaxNumber", 39.toString())
        buildConfigField(FieldSpec.Type.INT, "yellowLastIndexBeforeEnd", 29.toString())
        buildConfigField(FieldSpec.Type.INT, "yellowStart", 30.toString())
    }
}

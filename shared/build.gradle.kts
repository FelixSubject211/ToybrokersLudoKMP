import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.mokkery)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(projectDirPath)
                    }
                }
            }
        }
    }
    
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.toybrokers.ludo.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
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
        buildConfigField(FieldSpec.Type.INT, "maxDiceNumber", 6.toString())
        buildConfigField(FieldSpec.Type.INT, "trackMaxNumber", 39.toString())
        buildConfigField(FieldSpec.Type.INT, "endMaxNumber", 3.toString())
        buildConfigField(FieldSpec.Type.INT, "remainingAttempts", 2.toString())

        buildConfigField(FieldSpec.Type.INT, "redStart", 0.toString())
        buildConfigField(FieldSpec.Type.INT, "redLastIndexBeforeEnd", 39.toString())

        buildConfigField(FieldSpec.Type.INT, "blueStart", 10.toString())
        buildConfigField(FieldSpec.Type.INT, "blueLastIndexBeforeEnd", 9.toString())

        buildConfigField(FieldSpec.Type.INT, "greenStart", 20.toString())
        buildConfigField(FieldSpec.Type.INT, "greenLastIndexBeforeEnd", 19.toString())

        buildConfigField(FieldSpec.Type.INT, "yellowStart", 30.toString())
        buildConfigField(FieldSpec.Type.INT, "yellowLastIndexBeforeEnd", 29.toString())
    }

    targetConfigs {
        create("jvm") {
            buildConfigField(FieldSpec.Type.STRING, "target", "jvm")
        }
        create("ios") {
            buildConfigField(FieldSpec.Type.STRING, "target", "ios")
        }
        create("desktop") {
            buildConfigField(FieldSpec.Type.STRING, "desktopvalue", "desktop")
        }
        create("jsCommon") {
            buildConfigField(FieldSpec.Type.STRING, "target", "jsCommon")
        }
    }
}

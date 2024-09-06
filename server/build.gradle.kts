plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinx.rpc.platform)
    alias(libs.plugins.ktor)
    application
}

group = "com.toybrokers.ludo"
version = "1.0.0"

application {
    mainClass.set("com.toybrokers.ludo.ApplicationKt")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core.jvm)
    implementation(libs.kotlinx.rpc.krpc.ktor.server)
    implementation(libs.kotlinx.rpc.krpc.serialization.json)
    implementation(libs.kotlinx.rpc.krpc.server)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cors.jvm)
    implementation(libs.ktor.server.host.common.jvm)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.websockets.jvm)
    implementation(libs.logback)
    implementation(projects.shared)
    testImplementation(libs.kotlinx.rpc.krpc.client)
    testImplementation(libs.kotlinx.rpc.krpc.ktor.client)
    testImplementation(libs.ktor.server.tests)
}
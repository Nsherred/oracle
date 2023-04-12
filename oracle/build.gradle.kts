plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
}


val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

dependencies {
    implementation(project(":core"))
    implementation(project(":ecs"))
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    implementation("com.aallam.openai:openai-client:2.1.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
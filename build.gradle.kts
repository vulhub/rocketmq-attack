plugins {
    kotlin("jvm") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "org.vulhub"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.rocketmq:rocketmq-tools:5.1.0")
    implementation("com.github.ajalt.clikt:clikt:4.2.2")
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("org.vulhub.MainKt")
}

tasks.shadowJar {
    archiveClassifier.set("")
    mergeServiceFiles()
}

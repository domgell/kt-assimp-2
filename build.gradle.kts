plugins {
    kotlin("jvm") version "1.9.22"
}

group = "org.dom"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    implementation("org.lwjgl:lwjgl-assimp:3.3.3")
    implementation("org.joml:joml:1.10.5")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
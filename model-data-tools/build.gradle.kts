plugins {
    kotlin("jvm") version "1.3.71"
}

group = "com.headlessideas"
version = "0.1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io")}
}

dependencies {
    val jungVersion = "master-SNAPSHOT"

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jgrapht:jgrapht-core:1.4.0")
    implementation("org.jgrapht:jgrapht-io:1.4.0")
    implementation("com.github.ajalt:clikt:2.6.0")
    //implementation("com.strumenta.antlr-kotlin:antlr-kotlin-runtime-jvm:94f8764bcf")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
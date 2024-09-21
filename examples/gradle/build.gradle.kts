import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("application")
    id("com.github.ben-manes.versions") version "0.51.0"
    kotlin("jvm") version "2.0.20"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") } // only needed for SNAPSHOT
}

dependencies {
    implementation("net.thauvin.erik:isgd-shorten:1.0.2-SNAPSHOT")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClass.set("com.example.IsgdExampleKt")
}

kotlin {
    compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
}

tasks {
    register("runJava", JavaExec::class) {
        group = "application"
        mainClass.set("com.example.IsgdSample")
        classpath = sourceSets.main.get().runtimeClasspath
    }
}

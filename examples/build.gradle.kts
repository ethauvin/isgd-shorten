import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("application")
    id("com.github.ben-manes.versions") version "0.48.0"
    kotlin("jvm") version "1.9.10"
}

// ./gradlew run --args='https://www.example.com https://is.gd/Pt2sET'
// ./gradlew runJava --args='https://www.example.com https://is.gd/Pt2sET'

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") } // only needed for SNAPSHOT
}

dependencies {
    implementation("net.thauvin.erik:isgd-shorten:1.0.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClass.set("com.example.IsgdExampleKt")
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = java.targetCompatibility.toString()
    }

    register("runJava", JavaExec::class) {
        group = "application"
        mainClass.set("com.example.IsgdSample")
        classpath = sourceSets.main.get().runtimeClasspath
    }
}

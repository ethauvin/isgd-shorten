plugins {
    id("application")
    id("com.github.ben-manes.versions") version "0.39.0"
    kotlin("jvm") version "1.5.21"
}

// ./gradlew run --args='https://www.example.com https://is.gd/Pt2sET'
// ./gradlew runJava --args='https://www.example.com https://is.gd/Pt2sET'

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("net.thauvin.erik:isgd-shorten:0.9.2-SNAPSHOT")
}

application {
    mainClass.set("com.example.IsgdExampleKt")
}

tasks {
    register("runJava", JavaExec::class) {
        group = "application"
        mainClass.set("com.example.IsgdSample")
        classpath = sourceSets.main.get().runtimeClasspath
    }
}

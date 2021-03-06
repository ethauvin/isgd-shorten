plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.0"
    id("com.github.ben-manes.versions") version "0.29.0"
    application
}

// ./gradlew run --args='https://wwwcom.example. https://is.gd/Pt2sET'
// ./gradlew runJava --args='https://www.example.com https://is.gd/Pt2sET'

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation("net.thauvin.erik:isgd-shorten:0.9.1")
}

application {
    mainClassName = "com.example.IsgdExampleKt"
}

tasks {
    register("runJava", JavaExec::class) {
        group = "application"
        main = "com.example.IsgdSample"
        classpath = sourceSets["main"].runtimeClasspath
    }
}

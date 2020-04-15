plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.71"
    id("com.github.ben-manes.versions") version "0.28.0"
    application
}

// ./gradlew run --args='https://wwwcom.example. https://is.gd/Pt2sET'
// ./gradlew runJava --args='https://www.example.com https://is.gd/Pt2sET'

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("net.thauvin.erik:isgd-shorten:0.9.0-beta")
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

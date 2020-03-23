import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties

plugins {
    jacoco
    `java-library`
    `maven-publish`
    id("com.github.ben-manes.versions") version "0.28.0"
    id("com.jfrog.bintray") version "1.8.4"
    id("io.gitlab.arturbosch.detekt") version "1.7.0-beta2"
    id("net.thauvin.erik.gradle.semver") version "1.0.4"
    id("org.jetbrains.dokka") version "0.10.1"
    id("org.jetbrains.kotlin.jvm") version "1.3.70"
    id("org.jetbrains.kotlin.kapt") version "1.3.70"
    id("org.sonarqube") version "2.8"
}

group = "net.thauvin.erik"
description = "is.gd Shortener for Kotlin/Java"

val gitHub = "ethauvin/$name"
val mavenUrl = "https://github.com/$gitHub"
val deployDir = "deploy"
var isRelease = "release" in gradle.startParameter.taskNames

val publicationName = "mavenJava"

var semverProcessor = "net.thauvin.erik:semver:1.2.0"

// Load local.properties
File("local.properties").apply {
    if (exists()) {
        FileInputStream(this).use { fis ->
            Properties().apply {
                load(fis)
                forEach { (k, v) ->
                    extra[k as String] = v
                }
            }
        }
    }
}

repositories {
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

kapt {
    arguments {
        arg("semver.project.dir", projectDir)
    }
}

detekt {
    baseline = project.rootDir.resolve("detekt-baseline.xml")
}

jacoco {
    toolVersion = "0.8.5"
}

sonarqube {
    properties {
        property("sonar.projectKey", "ethauvin_$name")
        property("sonar.sourceEncoding", "UTF-8")
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    dependsOn(tasks.dokka)
    from(tasks.dokka)
    archiveClassifier.set("javadoc")
    description = "Assembles a JAR of the generated Javadoc."
    group = JavaBasePlugin.DOCUMENTATION_GROUP
}

tasks {
    withType<JacocoReport> {
        reports {
            xml.isEnabled = true
            html.isEnabled = true
        }
    }

    withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<GenerateMavenPom> {
        destination = file("$projectDir/pom.xml")
    }

    assemble {
        dependsOn(sourcesJar, javadocJar)
    }

    clean {
        doLast {
            project.delete(fileTree(deployDir))
        }
    }

    val copyToDeploy by registering(Copy::class) {
        from(configurations.runtimeClasspath) {
            exclude("annotations-*.jar")
        }
        from(jar)
        into(deployDir)
    }

    val bintrayUpload by existing(BintrayUploadTask::class) {
        dependsOn(publishToMavenLocal)
    }

    register("deploy") {
        description = "Copies all needed files to the $deployDir directory."
        group = PublishingPlugin.PUBLISH_TASK_GROUP
        dependsOn("build", "jar")
        outputs.dir(deployDir)
        inputs.files(copyToDeploy)
        mustRunAfter("clean")
    }

    register("release") {
        description = "Publishes version ${project.version} to Bintray."
        group = PublishingPlugin.PUBLISH_TASK_GROUP
        dependsOn("wrapper", bintrayUpload)
    }

    "sonarqube" {
        dependsOn("jacocoTestReport")
    }
}

fun findProperty(s: String) = project.findProperty(s) as String?
bintray {
    user = findProperty("bintray.user")
    key = findProperty("bintray.apikey")
    publish = isRelease
    setPublications(publicationName)
    pkg.apply {
        repo = "maven"
        name = project.name
        desc = description
        websiteUrl = mavenUrl
        issueTrackerUrl = "$mavenUrl/issues"
        githubRepo = gitHub
        githubReleaseNotesFile = "README.md"
        vcsUrl = "$mavenUrl.git"
        setLabels(
            "is.gd",
            "v.gd",
            "java",
            "kotlin",
            "shorten",
            "shorten-urls",
            "shortener",
            "shortener-service",
            "shortens-links",
            "shorturl",
            "url-shortener"
        )
        publicDownloadNumbers = true
        version.apply {
            name = project.version as String
            desc = description
            vcsTag = project.version as String
            gpg.apply {
                sign = true
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>(publicationName) {
            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)
            pom.withXml {
                asNode().apply {
                    appendNode("name", project.name)
                    appendNode("description", project.description)
                    appendNode("url", mavenUrl)

                    appendNode("licenses").appendNode("license").apply {
                        appendNode("name", "BSD 3-Clause")
                        appendNode("url", "https://opensource.org/licenses/BSD-3-Clause")
                    }

                    appendNode("developers").appendNode("developer").apply {
                        appendNode("id", "ethauvin")
                        appendNode("name", "Erik C. Thauvin")
                        appendNode("email", "erik@thauvin.net")
                    }

                    appendNode("scm").apply {
                        appendNode("connection", "scm:git:$mavenUrl.git")
                        appendNode("developerConnection", "scm:git:git@github.com:$gitHub.git")
                        appendNode("url", mavenUrl)
                    }

                    appendNode("issueManagement").apply {
                        appendNode("system", "GitHub")
                        appendNode("url", "$mavenUrl/issues")
                    }
                }
            }
        }
    }
}

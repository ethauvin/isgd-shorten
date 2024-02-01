[![License (3-Clause BSD)](https://img.shields.io/badge/license-BSD%203--Clause-blue.svg?style=flat-square)](https://opensource.org/licenses/BSD-3-Clause)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.21-7f52ff)](https://kotlinlang.org/)
[![bld](https://img.shields.io/badge/1.8.0-FA9052?label=bld&labelColor=2392FF)](https://rife2.com/bld)
[![release](https://img.shields.io/github/release/ethauvin/isgd-shorten.svg)](https://github.com/ethauvin/isgd-shorten/releases/latest)
[![Maven Central](https://img.shields.io/maven-central/v/net.thauvin.erik/isgd-shorten.svg?color=blue)](https://central.sonatype.com/artifact/net.thauvin.erik/isgd-shorten)
[![Nexus Snapshot](https://img.shields.io/nexus/s/net.thauvin.erik/isgd-shorten?label=snapshot&server=https%3A%2F%2Foss.sonatype.org%2F)](https://oss.sonatype.org/content/repositories/snapshots/net/thauvin/erik/isgd-shorten/)


[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ethauvin_isgd-shorten&metric=alert_status)](https://sonarcloud.io/dashboard?id=ethauvin_isgd-shorten)
[![GitHub CI](https://github.com/ethauvin/isgd-shorten/actions/workflows/bld.yml/badge.svg)](https://github.com/ethauvin/isgd-shorten/actions/workflows/bld.yml)
[![CircleCI](https://circleci.com/gh/ethauvin/isgd-shorten/tree/master.svg?style=shield)](https://circleci.com/gh/ethauvin/isgd-shorten/tree/master)

# [is.gd](https://is.gd/developers.php) Shortener for Kotlin, Java & Android

A simple implementation of the [is.gd](https://is.gd/) URL shortening and lookup [APIs](https://is.gd/developers.php).

## Examples (TL;DR)

```kotlin
import net.thauvin.erik.isgd.Isgd

...

Isgd.shorten("https://www.example.com/") // returns https://is.gd/Pt2sET
Isgd.lookup("https://is.gd/Pt2sET") // returns https://www.example.com

```

- View [bld](https://github.com/ethauvin/isgd-shorten/blob/master/examples/bld) or [Gradle](https://github.com/ethauvin/isgd-shorten/blob/master/examples/gradle) Examples.

### JSON or XML

The [is.gd API](https://is.gd/developers.php) can return data in plain text (default), JSON or XML.

```kotlin
Isgd.shorten("https://www.example.com/", format = Format.JSON)
```

returns:

```json
{ "shorturl": "https://is.gd/Pt2sET" }
```

### Parameters

All of the [is.gd API](https://is.gd/developers.php) parameters are supported:

```kotlin
Isgd.shorten(
    url = url,
    shorturl="foobar",
    callback = "test",
    logstats = true,
    format = Format.JSON)
```

returns:

```js
test({ "shorturl": "https://is.gd/foobar" });
```

### bld

To use with [bld](https://rife2.com/bld), include the following dependency in your [build](https://github.com/ethauvin/isgd-shorten/blob/master/examples/bld/src/bld/java/com/example/ExampleBuild.java) file:

```java
repositories = List.of(MAVEN_CENTRAL, SONATYPE_SNAPSHOTS_LEGACY);

scope(compile)
    .include("net.thauvin.erik:isgd-shorten:1.0.`");
```

### Gradle

To use with [Gradle](https://gradle.org/), include the following dependency in your [build](https://github.com/ethauvin/isgd-shorten/blob/master/examples/gradle/build.gradle.kts) file:

```gradle
repositories {
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") } // only needed for SNAPSHOT
}

dependencies {
    implementation("net.thauvin.erik:isgd-shorten:1.0.1")
}
```

Instructions for using with Maven, Ivy, etc. can be found on [Maven Central](https://central.sonatype.com/artifact/net.thauvin.erik/isgd-shorten).

## Java

To make it easier to use the library with Java, configuration builders are available:

```java
var config = new Config.Builder()
        .url("https://www.example.com/")
        .shortUrl("foobar")
        .callback("test")
        .logStats(true)
        .format(Format.JSON)
        .build();

Isgd.shorten(config);
```

```java
var config = new Config.Builder()
        .shortUrl("https://is.gd/Pt2sET")
        .format(Format.XML)
        .build();

Isgd.lookup(config);
```

### Errors

An `IsgdException` is thrown when an API error occurs. The error message (text, XML or JSON) and HTTP status code can be retrieved as follows:

```kotlin
try {
    Isgd.shorten("https://is.gd/Pt2sET") // already shorten
} catch (e: IsgdException)
    println("Status Code: ${e.statusCode}")
    println("${e.message})
}
```

```
Status Code: 400
Error: Sorry, the URL you entered is on our internal blacklist. It may have been used abusively in the past, or it may link to another URL redirection service.
```

### v.gd

Additionally, link can be shortened using [v.gd](https://v.gd/) by setting the `isVgd` flag:

```kotlin
Isgd.shorten("https://www.example.com/", isVgd = true) // returns https://v.gd/2z2ncj
```

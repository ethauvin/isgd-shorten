[![License (3-Clause BSD)](https://img.shields.io/badge/license-BSD%203--Clause-blue.svg?style=flat-square)](http://opensource.org/licenses/BSD-3-Clause)  
[![Known Vulnerabilities](https://snyk.io/test/github/ethauvin/isgd-shorten/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/ethauvin/isgd-shorten?targetFile=pom.xml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ethauvin_isgd-shorten&metric=alert_status)](https://sonarcloud.io/dashboard?id=ethauvin_isgd-shorten) [![Build Status](https://travis-ci.org/ethauvin/isgd-shorten.svg?branch=master)](https://travis-ci.org/ethauvin/isgd-shorten) [![CircleCI](https://circleci.com/gh/ethauvin/isgd-shorten/tree/master.svg?style=shield)](https://circleci.com/gh/ethauvin/isgd-shorten/tree/master)

# [is.gd](https://is.gd/developers.php) Shortener for Kotlin/Java.

A simple implementation of the [is.gd API](https://is.gd/developers.php).

## Examples (TL;DR)

```kotlin
import net.thauvin.erik.isgd.Isgd

...

Isgd.shorten("https://www.example.com/") // returns https://is.gd/Pt2sET
Isgd.lookup("https://is.gd/Pt2sET") // returns https://www.example.com

```

 - View [Kotlin](https://github.com/ethauvin/isgd-shorten/blob/master/examples/src/main/kotlin/com/example/IsgdExample.kt) or [Java](https://github.com/ethauvin/isgd-shorten/blob/master/examples/src/main/java/com/example/IsgdSample.java) Examples.


### JSON or XML

The [is.gd API](https://is.gd/developers.php) can return data in plain text (default), JSON or XML.

```kotlin
Isgd.shorten("https://www.example.com/", format = Format.JSON)
```
```json
{
    "shorturl": "https://is.gd/Pt2sET"
}
```

### Parameters

All of the [is.gd API](https://is.gd/developers.php) parameters are supported:

```kotlin
Isgd.shorten(url = url, shorturl="foobar", callback = "test", logstats = true, format = Format.JSON)
```
```json
{
    "shorturl": "https://is.gd/foobar"
}
```

### v.gd

Additionally, link can be shorten using v.gd by setting the `isVgd` flag:

```kotlin
Isgd.shorten("https://www.example.com/", isVgd = true) // returns https://v.gd/2z2ncj
```

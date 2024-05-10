/*
 * IsgdTest.kt
 *
 * Copyright 2020-2024 Erik C. Thauvin (erik@thauvin.net)
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *   Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *   Neither the name of this project nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.thauvin.erik.isgd

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IsgdTest {
    private val url = "https://www.example.com"
    private val shortUrl = "https://is.gd/Pt2sET"
    private val shortVgdUrl = "https://v.gd/2z2ncj"

    @Test
    fun testException() {
        assertFailsWith(
            message = "shorten(duplicate)",
            exceptionClass = IsgdException::class,
            block = { Isgd.shorten(shortUrl) }
        )

        try {
            Isgd.shorten(shortUrl)
        } catch (e: IsgdException) {
            assertThat(e, "shorten(duplicate)").all {
                prop(IsgdException::statusCode).isEqualTo(400)
                prop(IsgdException::message).isNotNull().startsWith("Error: ")
            }
        }
    }

    @Test
    fun testExceptionConfig() {
        assertFailsWith(
            message = "shorten(config:duplicate)",
            exceptionClass = IsgdException::class,
            block = { Isgd.shorten(Config.Builder().url(shortUrl).build()) }
        )
    }

    @Test
    fun testLookup() {
        assertFailsWith(
            message = "lookup(empty)",
            exceptionClass = IllegalArgumentException::class,
            block = { Isgd.lookup("") }
        )
    }

    @Test
    fun testLookupConfig() {
        assertFailsWith(
            message = "lookup(config:empty)",
            exceptionClass = IllegalArgumentException::class,
            block = { Isgd.lookup(Config.Builder().shortUrl("").build()) }
        )
    }

    @Test
    fun testLookupDefault() {
        assertEquals(url, Isgd.lookup(shortUrl))
        assertEquals(url, Isgd.lookup(shortVgdUrl, isVgd = true), "lookup(isVgd)")
    }

    @Test
    fun testLookupDefaultConfig() {
        assertEquals(url, Isgd.lookup(Config.Builder().shortUrl(shortUrl).build()), "lookup(config)")
        assertEquals(
            url, Isgd.lookup(
                Config.Builder().shortUrl(shortVgdUrl).isVgd(true).build()
            ), "lookup(config:isVgd)"
        )
    }

    @Test
    fun testLookupJson() {
        assertEquals("{ \"url\": \"$url\" }", Isgd.lookup(shortUrl, format = Format.JSON))

        assertEquals(
            "test({ \"url\": \"$url\" });",
            Isgd.lookup(shortUrl, callback = "test", format = Format.JSON),
            "lookup(callback)"
        )
    }

    @Test
    fun testLookupJsonConfig() {
        assertEquals(
            "{ \"url\": \"$url\" }",
            Isgd.lookup(Config.Builder().shortUrl(shortUrl).format(Format.JSON).build()), "lookup(config)"
        )

        assertEquals(
            "test({ \"url\": \"$url\" });",
            Isgd.lookup(Config.Builder().shortUrl(shortUrl).callback("test").format(Format.JSON).build()),
            "lookup(config:callback)"
        )
    }

    @Test
    fun testLookupXml() {
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><output><url>$url</url></output>",
            Isgd.lookup(shortUrl, format = Format.XML)
        )
    }

    @Test
    fun testLookupXmlConfig() {
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><output><url>$url</url></output>",
            Isgd.lookup(Config.Builder().shortUrl(shortUrl).format(Format.XML).build()),
            "lookup(config:xml)"
        )
    }

    @Test
    fun testShorten() {
        assertFailsWith(
            message = "shorten(empty)",
            exceptionClass = IllegalArgumentException::class,
            block = { Isgd.shorten("") }
        )

        assertFailsWith(
            message = "shorten(shorturl)",
            exceptionClass = IsgdException::class,
            block = { Isgd.shorten(url, shorturl = "test") }
        )
    }

    @Test
    fun testShortenConfig() {
        assertFailsWith(
            message = "shorten(config:empty)",
            exceptionClass = IllegalArgumentException::class,
            block = { Isgd.shorten(Config.Builder().url("").build()) }
        )

        assertFailsWith(
            message = "shorten(config:shorturl)",
            exceptionClass = IsgdException::class,
            block = { Isgd.shorten(Config.Builder(url).shortUrl("test").build()) }
        )
    }

    @Test
    fun testShortenDefault() {
        assertEquals(shortUrl, Isgd.shorten(url), "shorten(url)")
        assertEquals(shortVgdUrl, Isgd.shorten(url, isVgd = true), "shorten(isVgd)")
        assertThat(Isgd.shorten(url, logstats = true), "shorten(callback)").matches("https://is.gd/\\w{6}".toRegex())
    }

    @Test
    fun testShortenDefaultConfig() {
        assertEquals(shortUrl, Isgd.shorten(Config.Builder().url(url).build()), "shorten(config:url)")
        assertEquals(
            shortVgdUrl,
            Isgd.shorten(Config.Builder().url(url).isVgd(true).build()),
            "shorten(config:isVgd)"
        )
        assertThat(Isgd.shorten(Config.Builder().url(url).logStats(true).build()), "shorten(config:callback)")
            .matches("https://is.gd/\\w{6}".toRegex())
    }

    @Test
    fun testShortenJson() {
        assertEquals("{ \"shorturl\": \"$shortUrl\" }", Isgd.shorten(url, format = Format.JSON))
        assertEquals(
            "test({ \"shorturl\": \"$shortUrl\" });",
            Isgd.shorten(url, callback = "test", format = Format.JSON),
            "shorten(callback,json)"
        )
    }

    @Test
    fun testShortenJsonConfig() {
        assertEquals(
            "{ \"shorturl\": \"$shortUrl\" }",
            Isgd.shorten(Config.Builder().url(url).format(Format.JSON).build()), "shorten(config:json)"
        )
        assertEquals(
            "test({ \"shorturl\": \"$shortUrl\" });",
            Isgd.shorten(Config.Builder().url(url).callback("test").format(Format.JSON).build()),
            "shorten(config:callback,json)"
        )
    }

    @Test
    fun testShortenXml() {
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<output><shorturl>$shortUrl</shorturl></output>",
            Isgd.shorten(url, format = Format.XML)
        )
    }

    @Test
    fun testShortenXmlConfig() {
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<output><shorturl>$shortUrl</shorturl></output>",
            Isgd.shorten(Config.Builder().url(url).format(Format.XML).build()),
            "shorten(config:xml)"
        )
    }

    @Test
    fun testShortenWeb() {
        assertThat(Isgd.shorten(url, format = Format.WEB)).contains(shortUrl)
    }

    @Test
    fun testShortenWebConfig() {
        assertThat(Isgd.shorten(Config.Builder().url(url).format(Format.WEB).build()), "shorten(config:web)")
            .contains(shortUrl)
    }
}

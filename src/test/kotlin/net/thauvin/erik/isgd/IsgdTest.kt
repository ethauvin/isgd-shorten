/*
 * IsgdTest.kt
 *
 * Copyright (c) 2022, Erik C. Thauvin (erik@thauvin.net)
 * All rights reserved.
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
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.matches
import assertk.assertions.prop
import assertk.assertions.startsWith
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
    fun testLookup() {
        assertFailsWith(
            message = "lookup(empty)",
            exceptionClass = IllegalArgumentException::class,
            block = { Isgd.lookup("") }
        )
    }

    @Test
    fun testLookupDefault() {
        assertEquals(url, Isgd.lookup(shortUrl))
        assertEquals(url, Isgd.lookup(shortVgdUrl, isVgd = true), "lookup(isVgd)")
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
    fun testLookupXml() {
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><output><url>$url</url></output>",
            Isgd.lookup(shortUrl, format = Format.XML)
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
    fun testShortenDefault() {
        assertEquals(shortUrl, Isgd.shorten(url), "shorten(url)")
        assertEquals(shortVgdUrl, Isgd.shorten(url, isVgd = true), "shorten(isVgd)")
        assertThat(Isgd.shorten(url, logstats = true), "shorten(callback)").matches("https://is.gd/\\w{6}".toRegex())


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
    fun testShortenXml() {
        assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<output><shorturl>$shortUrl</shorturl></output>",
            Isgd.shorten(url, format = Format.XML)
        )
    }

    @Test
    fun testShortenWeb() {
        assertThat(Isgd.shorten(url, format = Format.WEB)).contains(shortUrl)
    }
}

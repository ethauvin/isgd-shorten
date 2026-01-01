/*
 * ShortenConfigTests.kt
 *
 * Copyright 2020-2026 Erik C. Thauvin (erik@thauvin.net)
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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ShortenConfigTests {
    @Test
    fun `ShortenConfig default values`() {
        val config = ShortenConfig.Builder("https://example.com").build()

        assertEquals("https://example.com", config.url)
        assertEquals("", config.shorturl)
        assertEquals("", config.callback)
        assertFalse(config.logstats)
        assertEquals(Format.SIMPLE, config.format)
        assertFalse(config.isVgd)
    }

    @Test
    fun `ShortenConfig with custom shorturl`() {
        val config = ShortenConfig.Builder("https://example.com")
            .shorturl("https://short.url/cust")
            .build()

        assertEquals("https://example.com", config.url)
        assertEquals("https://short.url/cust", config.shorturl)
        assertEquals("", config.callback)
        assertFalse(config.logstats)
        assertEquals(Format.SIMPLE, config.format)
        assertFalse(config.isVgd)
    }

    @Test
    fun `ShortenConfig with custom callback`() {
        val config = ShortenConfig.Builder("https://example.com")
            .callback("myCallbackFunction")
            .build()

        assertEquals("https://example.com", config.url)
        assertEquals("", config.shorturl)
        assertEquals("myCallbackFunction", config.callback)
        assertFalse(config.logstats)
        assertEquals(Format.SIMPLE, config.format)
        assertFalse(config.isVgd)
    }

    @Test
    fun `ShortenConfig with logstats enabled`() {
        val config = ShortenConfig.Builder("https://example.com")
            .logstats(true)
            .build()

        assertEquals("https://example.com", config.url)
        assertEquals("", config.shorturl)
        assertEquals("", config.callback)
        assertTrue(config.logstats)
        assertEquals(Format.SIMPLE, config.format)
        assertFalse(config.isVgd)
    }

    @Test
    fun `ShortenConfig with custom format`() {
        val config = ShortenConfig.Builder("https://example.com")
            .format(Format.SIMPLE)
            .build()

        assertEquals("https://example.com", config.url)
        assertEquals("", config.shorturl)
        assertEquals("", config.callback)
        assertFalse(config.logstats)
        assertEquals(Format.SIMPLE, config.format)
        assertFalse(config.isVgd)
    }

    @Test
    fun `ShortenConfig with isVgd enabled`() {
        val config = ShortenConfig.Builder("https://example.com")
            .isVgd(true)
            .build()

        assertEquals("https://example.com", config.url)
        assertEquals("", config.shorturl)
        assertEquals("", config.callback)
        assertFalse(config.logstats)
        assertEquals(Format.SIMPLE, config.format)
        assertTrue(config.isVgd)
    }

    @Test
    fun `ShortenConfig with multiple custom values`() {
        val config = ShortenConfig.Builder("https://example.com")
            .shorturl("https://short.url/multi")
            .callback("multiCallback")
            .logstats(true)
            .format(Format.JSON)
            .isVgd(true)
            .build()

        assertEquals("https://example.com", config.url)
        assertEquals("https://short.url/multi", config.shorturl)
        assertEquals("multiCallback", config.callback)
        assertTrue(config.logstats)
        assertEquals(Format.JSON, config.format)
        assertTrue(config.isVgd)
    }
}

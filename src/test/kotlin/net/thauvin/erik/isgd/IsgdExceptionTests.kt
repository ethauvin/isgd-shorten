/*
 * IsgdExceptionTests.kt
 *
 * Copyright 2020-2025 Erik C. Thauvin (erik@thauvin.net)
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
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import assertk.assertions.startsWith
import kotlin.test.Test
import kotlin.test.assertFailsWith

class IsgdExceptionTests {
    private val shortUrl = "https://is.gd/Pt2sET"

    @Test
    fun `Duplicate URL`() {
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
    fun `Duplicate URL with config`() {
        assertFailsWith(
            message = "shorten(config:duplicate)",
            exceptionClass = IsgdException::class,
            block = { Isgd.shorten(ShortenConfig.Builder(shortUrl).build()) }
        )
    }

    @Test
    fun `Lookup empty string`() {
        assertFailsWith(
            message = "lookup(empty)",
            exceptionClass = IllegalArgumentException::class,
            block = { Isgd.lookup("") }
        )
    }

    @Test
    fun `Lookup empty string with config`() {
        assertFailsWith(
            message = "lookup(config:empty)",
            exceptionClass = IllegalArgumentException::class,
            block = { Isgd.lookup(LookupConfig.Builder("").build()) }
        )
    }

    @Test
    fun `Shorten empty string`() {
        assertFailsWith(
            message = "shorten(empty)",
            exceptionClass = IllegalArgumentException::class,
            block = { Isgd.shorten("") }
        )
    }

    @Test
    fun `Shorten empty string with config`() {
        assertFailsWith(
            message = "shorten(config:empty)",
            exceptionClass = IllegalArgumentException::class,
            block = { Isgd.shorten(ShortenConfig.Builder("").build()) }
        )
    }

    @Test
    fun `Verify exception with status code and message`() {
        val exception = IsgdException(404, "Not Found")
        assertThat(exception).all {
            prop(IsgdException::statusCode).isEqualTo(404)
            prop(IsgdException::message).isNotNull().isEqualTo("Not Found")
        }
    }
}

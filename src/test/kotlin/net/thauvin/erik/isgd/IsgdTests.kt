/*
 * IsgdTests.kt
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

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class IsgdTests {
    private val url = "https://www.example.com"
    private val shortUrl = "https://is.gd/Pt2sET"
    private val shortVgdUrl = "https://v.gd/2z2ncj"

    @Nested
    @DisplayName("Configuration Tests")
    inner class ConfigurationTests {
        @Test
        fun `Validate Lookup Configuration`() {
            val config = LookupConfig.Builder("foo")

            // Defaults
            assertAll {
                assertThat(config.shorturl).isEqualTo("foo")
                assertThat(config.format).isEqualTo(Format.SIMPLE)
                assertThat(config.callback).isEmpty()
                assertThat(config.isVgd).isFalse()
            }

            config.format(Format.JSON)
                .callback("callback")
                .isVgd(true)

            assertAll {
                assertThat(config.shorturl).isEqualTo("foo")
                assertThat(config.format).isEqualTo(Format.JSON)
                assertThat(config.callback).isEqualTo("callback")
                assertThat(config.isVgd).isTrue()
            }

            config.shorturl(shortUrl).isVgd(false)

            assertAll {
                assertThat(config.shorturl).isEqualTo(shortUrl)
                assertThat(config.isVgd).isFalse()
            }
        }

        @Test
        fun `Validate Shorten Configuration`() {
            val config = ShortenConfig.Builder("foo")

            // Defaults
            assertAll {
                assertThat(config.format).isEqualTo(Format.SIMPLE)
                assertThat(config.callback).isEmpty()
                assertThat(config.isVgd).isFalse()
                assertThat(config.logstats).isFalse()
                assertThat(config.shorturl).isEmpty()
                assertThat(config.url).isEqualTo("foo")
            }


            config.format(Format.JSON)
                .callback("callback")
                .isVgd(true)
                .logstats(true)

            assertAll {
                assertThat(config.format).isEqualTo(Format.JSON)
                assertThat(config.callback).isEqualTo("callback")
                assertThat(config.isVgd).isTrue()
                assertThat(config.logstats).isTrue()
                assertThat(config.url).isEqualTo("foo")
            }

            config.shorturl(shortUrl)
                .isVgd(false)
                .logstats(false)
                .url(url)

            assertAll {
                assertThat(config.shorturl).isEqualTo(shortUrl)
                assertThat(config.isVgd).isFalse()
                assertThat(config.logstats).isFalse()
                assertThat(config.url).isEqualTo(url)
            }
        }
    }

    @Nested
    @DisplayName("Encode Tests")
    inner class EncodeTests {
        @Test
        fun `Encode empty string`() {
            val emptyString = ""
            assertEquals(emptyString, emptyString.encode())
        }

        @Test
        fun `Encode non-alphanumeric ascii characters`() {
            val originalString = "``{|}~[]\\\"'"
            // ` -> %60
            // { -> %7B
            // | -> %7C
            // } -> %7D
            // ~ -> %7E
            // [ -> %5B
            // ] -> %5D
            // \ -> %5C
            // " -> %22
            // ' -> %27
            val expectedEncodedString = "%60%60%7B%7C%7D%7E%5B%5D%5C%22%27"
            assertEquals(expectedEncodedString, originalString.encode())
        }

        @Test
        fun `Encode simple string with no special characters`() {
            val simpleString = "HelloWorld123"
            assertEquals(simpleString, simpleString.encode())
        }

        @Test
        fun `Encode string containing ampersand character`() {
            val originalString = "key1=val1&key2=val2"
            val expectedEncodedString = "key1%3Dval1%26key2%3Dval2"
            assertEquals(expectedEncodedString, originalString.encode())
        }

        @Test
        fun `Encode string containing forward slash`() {
            val originalString = "path/to/resource"
            val expectedEncodedString = "path%2Fto%2Fresource"
            assertEquals(expectedEncodedString, originalString.encode())
        }

        @Test
        fun `Encode string containing hash character`() {
            val originalString = "section#details"
            val expectedEncodedString = "section%23details"
            assertEquals(expectedEncodedString, originalString.encode())
        }

        @Test
        fun `Encode string containing percent character`() {
            val originalString = "discount_10%"
            // The '%' character itself is encoded to '%25'
            val expectedEncodedString = "discount_10%25"
            assertEquals(expectedEncodedString, originalString.encode())
        }

        @Test
        fun `Encode string containing plus character`() {
            val originalString = "a+b=c"
            // '+' itself needs to be encoded to '%2B'
            // '=' needs to be encoded to '%3D'
            val expectedEncodedString = "a%2Bb%3Dc"
            assertEquals(expectedEncodedString, originalString.encode())
        }

        @Test
        fun `Encode string containing question mark`() {
            val originalString = "query?param=value"
            val expectedEncodedString = "query%3Fparam%3Dvalue"
            assertEquals(expectedEncodedString, originalString.encode())
        }

        @Test
        fun `Encode string with multiple common special characters`() {
            val originalString = "user@example.com?subject=Hello World!"
            // @ -> %40
            // . -> . (no change)
            // ? -> %3F
            // = -> %3D
            // space -> %20
            // ! -> %21
            val expectedEncodedString = "user%40example.com%3Fsubject%3DHello%20World%21"
            assertEquals(expectedEncodedString, originalString.encode())
        }

        @Test
        fun `Encode string with spaces`() {
            val originalString = "Hello World with spaces"
            val expectedEncodedString = "Hello%20World%20with%20spaces"
            assertEquals(expectedEncodedString, originalString.encode())
        }

        @Test
        fun `Encode string with unicode characters`() {
            val originalString = "你好世界" // "Hello World" in Chinese
            val expectedEncodedString = "%E4%BD%A0%E5%A5%BD%E4%B8%96%E7%95%8C"
            assertEquals(expectedEncodedString, originalString.encode())
        }
    }

    @Nested
    @DisplayName("Format Tests")
    inner class FormatTest {
        @Test
        fun `Format JSON`() {
            val expected = "json"
            val actual = Format.JSON.type
            assertEquals(expected, actual, "Expected the 'type' value for Format.JSON to be 'json'")
        }

        @Test
        fun `Format SIMPLE`() {
            val expected = "simple"
            val actual = Format.SIMPLE.type
            assertEquals(expected, actual, "Expected the 'type' value for Format.SIMPLE to be 'simple'")
        }

        @Test
        fun `Format WEB`() {
            val expected = "web"
            val actual = Format.WEB.type
            assertEquals(expected, actual, "Expected the 'type' value for Format.WEB to be 'web'")
        }

        @Test
        fun `Format XML`() {
            val expected = "xml"
            val actual = Format.XML.type
            assertEquals(expected, actual, "Expected the 'type' value for Format.XML to be 'xml'")
        }
    }

    @Nested
    @DisplayName("Get Host Tests")
    inner class GetHostTests {
        @Test
        fun `Get default host`() {
            val result = Isgd.getHost()
            assertEquals("is.gd", result)
        }

        @Test
        fun `Get host when isVgd is false`() {
            val result = Isgd.getHost(false)
            assertEquals("is.gd", result)
        }

        @Test
        fun `Get host when isVgd is true`() {
            val result = Isgd.getHost(true)
            assertEquals("v.gd", result)
        }
    }

    @Nested
    @DisplayName("Lookup Tests")
    inner class LookupTests {
        @Test
        fun `Lookup as JSON`() {
            assertEquals("{ \"url\": \"$url\" }", Isgd.lookup(shortUrl, format = Format.JSON))
        }

        @Test
        fun `Lookup as JSON with callback`() {
            assertEquals(
                "test({ \"url\": \"$url\" });",
                Isgd.lookup(shortUrl, callback = "test", format = Format.JSON),
                "lookup(callback)"
            )
        }

        @Test
        fun `Lookup as JSON with config`() {
            assertEquals(
                "{ \"url\": \"$url\" }",
                Isgd.lookup(
                    LookupConfig.Builder(shortUrl).format(Format.JSON).build()
                ), "lookup(config)"


            )
        }

        @Test
        fun `Lookup as JSON with config and callback`() {
            assertEquals(
                "test({ \"url\": \"$url\" });",
                Isgd.lookup(
                    LookupConfig.Builder(shortUrl).callback("test").format(Format.JSON).build()
                ),
                "lookup(config:callback)"
            )
        }

        @Test
        fun `Lookup as plain text`() {
            assertEquals(
                url,
                Isgd.lookup(shortUrl, format = Format.SIMPLE)
            )
        }

        @Test
        fun `Lookup as plain text with config`() {
            assertEquals(
                url,
                Isgd.lookup(LookupConfig.Builder(shortUrl).format(Format.SIMPLE).build()),
                "lookup(config:simple)"
            )
        }

        @Test
        fun `Lookup as XML`() {
            assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><output><url>$url</url></output>",
                Isgd.lookup(shortUrl, format = Format.XML)
            )
        }

        @Test
        fun `Lookup as XML with config`() {
            assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><output><url>$url</url></output>",
                Isgd.lookup(LookupConfig.Builder(shortUrl).format(Format.XML).build()),
                "lookup(config:xml)"
            )
        }

        @Test
        fun `Lookup shorten URL`() {
            assertEquals(url, Isgd.lookup(shortUrl))
        }

        @Test
        fun `Lookup shorten URL as vgd`() {
            assertEquals(url, Isgd.lookup(shortVgdUrl, isVgd = true), "lookup(isVgd)")
        }

        @Test
        fun `Lookup shorten URL with config`() {
            assertEquals(
                url, Isgd.lookup(
                    LookupConfig.Builder(shortUrl).build()
                ), "lookup(config)"
            )
        }

        @Test
        fun `Lookup shorten URL with config as vgd`() {
            assertEquals(
                url, Isgd.lookup(
                    LookupConfig.Builder(shortVgdUrl).isVgd(true).build()
                ), "lookup(config:isVgd)"
            )
        }
    }

    @Nested
    @DisplayName("Shorten Tests")
    inner class ShortenTests {
        @Test
        fun `Shorten as JSON`() {
            assertEquals("{ \"shorturl\": \"$shortUrl\" }", Isgd.shorten(url, format = Format.JSON))
        }

        @Test
        fun `Shorten as JSON with callback`() {
            assertEquals(
                "test({ \"shorturl\": \"$shortUrl\" });",
                Isgd.shorten(url, callback = "test", format = Format.JSON),
                "shorten(callback,json)"
            )
        }

        @Test
        fun `Shorten as JSON with config`() {
            assertEquals(
                "{ \"shorturl\": \"$shortUrl\" }",
                Isgd.shorten(
                    ShortenConfig.Builder(url).format(Format.JSON).build()
                ), "shorten(config:json)"
            )
        }

        @Test
        fun `Shorten as JSON with config and callback`() {
            assertEquals(
                "test({ \"shorturl\": \"$shortUrl\" });",
                Isgd.shorten(
                    ShortenConfig.Builder(url).callback("test").format(Format.JSON).build()
                ),
                "shorten(config:callback,json)"
            )
        }

        @Test
        fun `Shorten as WEB`() {
            assertThat(Isgd.shorten(url, format = Format.WEB)).contains(shortUrl)
        }

        @Test
        fun `Shorten as WEB with config`() {
            assertThat(
                Isgd.shorten(
                    ShortenConfig.Builder(url).format(Format.WEB).build()
                ), "shorten(config:web)"
            )
                .contains(shortUrl)
        }

        @Test
        fun `Shorten as plain text`() {
            assertThat(Isgd.shorten(url, format = Format.SIMPLE)).isEqualTo(shortUrl)
        }

        @Test
        fun `Shorten as plain text with config`() {
            assertThat(
                Isgd.shorten(
                    ShortenConfig.Builder(url).format(Format.SIMPLE).build()
                ), "shorten(config:simple)"
            )
                .isEqualTo(shortUrl)
        }

        @Test
        fun `Shorten as XML`() {
            assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                        "<output><shorturl>$shortUrl</shorturl></output>",
                Isgd.shorten(url, format = Format.XML)
            )
        }

        @Test
        fun `Shorten as XML with config`() {
            assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                        "<output><shorturl>$shortUrl</shorturl></output>",
                Isgd.shorten(ShortenConfig.Builder(url).format(Format.XML).build()),
                "shorten(config:xml)"
            )
        }

        @Test
        fun `Shorten URL`() {
            assertEquals(shortUrl, Isgd.shorten(url), "shorten(url)")
        }

        @Test
        fun `Shorten URL as vgd`() {
            assertEquals(shortVgdUrl, Isgd.shorten(url, isVgd = true), "shorten(isVgd)")
            assertThat(
                Isgd.shorten(url, logstats = true),
                "shorten(callback)"
            ).matches("https://is.gd/\\w{6}".toRegex())
        }

        @Test
        fun `Shorten URL with config`() {
            assertEquals(
                shortUrl, Isgd.shorten(
                    ShortenConfig.Builder(url).build()
                ), "shorten(config:url)"
            )
        }

        @Test
        fun `Shorten URL with config as vgd`() {
            assertEquals(
                shortVgdUrl,
                Isgd.shorten(ShortenConfig.Builder(url).isVgd(true).build()),
                "shorten(config:isVgd)"
            )
        }

        @Test
        fun `Shorten URL with config and log stats`() {
            assertThat(
                Isgd.shorten(
                    ShortenConfig.Builder(url).logstats(true).build()
                ), "shorten(config:logstats)"
            )
                .matches("https://is.gd/\\w{6}".toRegex())
        }
    }
}

/*
 * Isgd.kt
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

import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * See the [is.gd API](https://is.gd/apishorteningreference.php).
 */
enum class Format(val type: String) {
    WEB("web"), SIMPLE("simple"), XML("xml"), JSON("json")
}

fun String.encode(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.name())
}

/**
 * Implements the [is.gd API](https://is.gd/developers.php).
 */
class Isgd private constructor() {
    companion object {
        private fun callApi(url: String): String {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Linux x86_64; rv:89.0) Gecko/20100101 Firefox/89.0"
            )
            if (connection.responseCode in 200..399) {
                return connection.inputStream.bufferedReader().readText()
            } else {
                throw IsgdException(connection.responseCode, connection.errorStream.bufferedReader().readText())
            }
        }

        private fun getHost(isVgd: Boolean = false): String {
            return if (isVgd) "v.gd" else "is.gd"
        }

        /**
         * See the [is.gd API](https://is.gd/apilookupreference.php).
         */
        @JvmStatic
        @JvmOverloads
        @Throws(IsgdException::class)
        fun lookup(
                shorturl: String,
                callback: String = "",
                format: Format = Format.SIMPLE,
                isVgd: Boolean = false
        ): String {
            if (shorturl.isEmpty()) {
                throw IllegalArgumentException("Please specify a valid short URL to lookup.")
            }

            val sb = StringBuilder("https://${getHost(isVgd)}/forward.php?shorturl=${shorturl.encode()}")

            if (callback.isNotEmpty()) {
                sb.append("&callback=${callback.encode()}")
            }

            sb.append("&format=${format.type.encode()}")

            return callApi(sb.toString())
        }

        /**
         * See the [is.gd API](https://is.gd/apishorteningreference.php).
         */
        @JvmStatic
        @JvmOverloads
        @Throws(IsgdException::class)
        fun shorten(
                url: String,
                shorturl: String = "",
                callback: String = "",
                logstats: Boolean = false,
                format: Format = Format.SIMPLE,
                isVgd: Boolean = false
        ): String {
            if (url.isEmpty()) {
                throw IllegalArgumentException("Please enter a valid URL to shorten.")
            }

            val sb = StringBuilder("https://${getHost(isVgd)}/create.php?url=${url.encode()}")

            if (shorturl.isNotEmpty()) {
                sb.append("&shorturl=${shorturl.encode()}")
            }

            if (callback.isNotEmpty()) {
                sb.append("&callback=${callback.encode()}")
            }

            if (logstats) {
                sb.append("&logstats=1")
            }

            sb.append("&format=${format.type.encode()}")

            return callApi(sb.toString())
        }
    }
}

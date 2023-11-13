/*
 * Config.kt
 *
 * Copyright 2020-2023 Erik C. Thauvin (erik@thauvin.net)
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

/**
 * Provides a builder to create/lookup an is.gd shortlink.
 */
class Config private constructor(
    val url: String,
    val shorturl: String,
    val callback: String,
    val logstats: Boolean,
    val format: Format,
    val isVgd: Boolean
) {
    /**
     * Configures the parameters to create/lookup an is.gd shortlink.
     *
     * See the [is.gd API](https://is.gd/apishorteningreference.php).
     */
    data class Builder(
        private var url: String = "",
        private var shorturl: String = "",
        private var callback: String = "",
        private var logstats: Boolean = false,
        private var format: Format = Format.SIMPLE,
        private var isVgd: Boolean = false
    ) {
        fun url(url: String) = apply { this.url = url }
        fun shortUrl(shortUrl: String) = apply { this.shorturl = shortUrl }
        fun callback(callback: String) = apply { this.callback = callback }
        fun logStats(logStats: Boolean) = apply { this.logstats = logStats }
        fun format(format: Format) = apply { this.format = format }
        fun isVgd(isVgd: Boolean) = apply { this.isVgd = isVgd }

        fun build() = Config(
            url,
            shorturl,
            callback,
            logstats,
            format,
            isVgd
        )
    }
}

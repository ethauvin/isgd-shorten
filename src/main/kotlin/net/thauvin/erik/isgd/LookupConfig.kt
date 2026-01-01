/*
 * LookupConfig.kt
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

/**
 * Provides a builder to lookup an is.gd shortlink.
 */
class LookupConfig private constructor(builder: Builder) {
    val shorturl: String = builder.shorturl
    val callback: String = builder.callback
    val format: Format = builder.format
    val isVgd: Boolean = builder.isVgd

    /**
     * Configures the parameters to lookup an is.gd shortlink.
     *
     * See the [is.gd Lookup]() API.
     *
     * @param shorturl The shorturl parameter is the shortened is.gd URL that you want to look up. You can either submit
     * the full address (e.g. `https://is.gd/example`) or only the unique part (e.g. `example`). The address you submit
     * should be properly formed; the API lookup function is not guaranteed to handle malformed URLs the same way as
     * when you visit them manually.
     */
    data class Builder(var shorturl: String) {
        var callback: String = ""
        var format: Format = Format.SIMPLE
        var isVgd: Boolean = false

        /**
         * The shorturl parameter is the shortened is.gd URL that you want to look up. You can either submit the full
         * address (e.g. `https://is.gd/example`) or only the unique part (e.g. `example`). The address you submit
         * should be properly formed; the API lookup function is not guaranteed to handle malformed URLs the same way
         * as when you visit them manually.
         */
        fun shorturl(shorturl: String): Builder = apply { this.shorturl = shorturl }

        /**
         * The callback parameter is used to specify a callback function to wrap the returned data in
         * when using JSON format. This can be useful when working with cross domain data. Even when using JSON format
         * this parameter is optional.
         */
        fun callback(callback: String): Builder = apply { this.callback = callback }

        /**
         * The format parameter determines what format is.gd uses to send output back to you (e.g. to
         * tell you what your new shortened URL is or if an error has occurred).
         */
        fun format(format: Format): Builder = apply { this.format = format }

        /**
         * Shorten using the `v.gd` domain.
         */
        fun isVgd(isVgd: Boolean): Builder = apply { this.isVgd = isVgd }

        /**
         * Builds a new configuration.
         */
        fun build() = LookupConfig(this)
    }
}

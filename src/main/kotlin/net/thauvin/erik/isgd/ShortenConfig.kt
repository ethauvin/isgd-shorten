/*
 * Config.kt
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

/**
 * Provides a builder to create/lookup an is.gd shortlink.
 */
class Config private constructor(builder: Builder) {
    val url: String = builder.url
    val shorturl: String = builder.shorturl
    val callback: String = builder.callback
    val logstats: Boolean = builder.logstats
    val format: Format = builder.format
    val isVgd: Boolean = builder.isVgd

    /**
     * Configures the parameters to create/lookup an is.gd shortlink.
     *
     * See the [is.gd Shortening](https://is.gd/apishorteningreference.php) or
     * [is.gd Lookup](https://is.gd/apilookupreference.php) APIs.
     */
    data class Builder(var url: String) {
        var shorturl: String = "",
        var callback: String = "",
        var logstats: Boolean = false,
        var format: Format = Format.SIMPLE,
        var isVgd: Boolean = false

        /**
         * The url parameter is the address that you want to shorten.
         */
        fun url(url: String): Builder = apply { this.url = url }

        /**
         * You can specify the shorturl parameter if you'd like to pick a shortened URL instead of
         * having is.gd randomly generate one. These must be between 5 and 30 characters long and can only contain
         * alphanumeric characters and underscores. Shortened URLs are case-sensitive. Bear in mind that a desired
         * short URL might already be taken (this is very often the case with common words) so if you're using this
         * option be prepared to respond to an error and get an alternative choice from your app's user.
         */
        fun shortUrl(shortUrl: String): Builder = apply { this.shorturl = shortUrl }

        /**
         * The callback parameter is used to specify a callback function to wrap the returned data in
         * when using JSON format. This can be useful when working with cross domain data. Even when using JSON format
         * this parameter is optional.
         */
        fun callback(callback: String): Builder = apply { this.callback = callback }

        /**
         * Turns on logging of detailed statistics when the shortened URL you create is accessed. This
         * allows you to see how many times the link was accessed on a given day, what pages referred people to the
         * link, what browser visitors were using etc. You can access these stats via the link preview page for your
         * shortened URL (add a hyphen/dash to the end of the shortened URL to get to it). Creating links with
         * statistics turned on has twice the "cost" towards our rate limit of other shortened links, so leave this
         * parameter out of your API call if you don't require statistics on usage. See the
         * [usage limits page](https://is.gd/usagelimits.php) for more information on this.
         */
        fun logStats(logStats: Boolean): Builder = apply { this.logstats = logStats }

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
        fun build() = Config(this)
    }
}

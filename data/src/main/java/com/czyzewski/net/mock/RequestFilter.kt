package com.czyzewski.net.mock


import okhttp3.Headers
import okhttp3.Request
import okhttp3.RequestBody
import okio.Buffer

data class RequestFilter(
    val path: Path,
    val method: Method = Method.GET,
    val body: Body = Body.Any,
    val headers: Headers = Headers.headersOf(),
    /**
     * Whether filter has priority over other filters with same match score.
     */
    var priority: Boolean = false
) {

    fun matches(request: Request): Boolean {
        val query = request.url.query?.let { "?$it" }.orEmpty()
        val pathWithQuery = request.url.encodedPath + query
        return path.matches(pathWithQuery) &&
                method == getMethodOrDefault(request.method) &&
                body.matches(request.body?.asString()) &&
                request.headers.toSet().containsAll(headers.toSet())
    }

    fun matchScore(request: Request): Int {
        if (!matches(request)) {
            return 0
        }
        val pathScore = when (path) {
            is Path.Regex -> 1
            is Path.Exact -> 2
        }
        val bodyScore = when (body) {
            is Body.Any -> 1
            is Body.Regex -> 2
            is Body.Exact -> 3
        }
        return pathScore + bodyScore
    }

    private fun getMethodOrDefault(method:String) =
        try {
            Method.valueOf(method)
        } catch (e:IllegalArgumentException) {
            Method.GET
        }
}

enum class Method {
    GET, POST, PUT, PATCH, DELETE
}

sealed class Path {

    abstract fun matches(string: String?): Boolean

    data class Exact(val path: String?) : Path() {
        override fun matches(string: String?): Boolean {
            return path == string
        }
    }
    data class Regex(var regex: String) : Path() {
        override fun matches(string: String?): Boolean {
            return regex.toRegex().matches(string.orEmpty())
        }
    }
}

sealed class Body {

    abstract fun matches(string: String?): Boolean

    object Any : Body() {
        override fun matches(string: String?): Boolean {
            return true
        }
    }
    data class Exact(val body: String?) : Body() {
        override fun matches(string: String?): Boolean {
            return body == string || (body.isNullOrBlank() && string.isNullOrBlank())
        }
    }
    data class Regex(var regex: String) : Body() {
        override fun matches(string: String?): Boolean {
            return regex.toRegex().matches(string.orEmpty())
        }
    }
}

private fun RequestBody.asString(): String {
    val buffer = Buffer()
    writeTo(buffer)
    return buffer.readUtf8()
}

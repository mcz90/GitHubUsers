package com.czyzewski.net.mock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import java.net.ServerSocket


internal fun MockWebServer.configure(): MockWebServer {

    GlobalScope.launch {
        val freePort = ServerSocket(0).use { it.localPort }
        start(freePort)
    }

    val localhostCertificate = HeldCertificate.Builder()
        .addSubjectAlternativeName("127.0.0.1")
        .build()

    val serverCertificates = HandshakeCertificates.Builder()
        .heldCertificate(localhostCertificate)
        .build()

    useHttps(serverCertificates.sslSocketFactory(), false)
    return this
}

class MockInterceptor(
    private val mocks: Map<RequestFilter, MockResponse> = emptyMap(),
    private val mockServer: MockWebServer = MockWebServer().configure()
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        fun findMockResponse(request: Request): MockResponse? {
            val matchedFilters = mocks.keys
                .groupBy { filter -> filter.matchScore(request) }
                .filter { (score, _) -> score > 0 } // find all filters that match
                .maxBy { (score, _) -> score } // get list of filters with highest match score
                ?.let { (_, filters) -> filters }
            val filter = matchedFilters
                ?.find { it.priority } // get prioritized filter
                ?: matchedFilters?.firstOrNull() // or first matched
            return mocks[filter]
        }

        fun Interceptor.Chain.findServer(): HttpUrl =
            when (val mockResponse = findMockResponse(request())) {
                is MockResponse -> {
                    mockResponse.addHeader(
                        "Mock",
                        "<-- Real request ${request().url} is mocked -->"
                    )
                    mockServer.enqueue(mockResponse)
                    request().url.newBuilder()
                        .host(mockServer.hostName)
                        .port(mockServer.port)
                        .build()
                }
                else -> request().url
            }

        return with(chain) {
            proceed(
                request().newBuilder()
                    .url(findServer())
                    .build()
            )
        }
    }

}

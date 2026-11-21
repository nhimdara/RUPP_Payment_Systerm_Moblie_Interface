package kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote

import com.google.gson.Gson
import com.google.gson.JsonObject
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.BuildConfig
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.RefreshTokenRequest
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Builds the Retrofit service. Adds the bearer token to every call except the public
 * auth endpoints, and on a 401 tries POST /auth/refresh once before sending the user to Login
 * (as recommended in section 1.4 of the API guide).
 */
class ApiClient(private val session: SessionStore) {

    private val gson = Gson()
    private var cachedUrl: String? = null
    private var cachedService: RuppApiService? = null

    val service: RuppApiService
        @Synchronized get() {
            val url = session.baseUrl
            if (cachedService == null || cachedUrl != url) {
                cachedService = build(url)
                cachedUrl = url
            }
            return cachedService!!
        }

    private fun build(baseUrl: String): RuppApiService {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(AuthHeaderInterceptor())
            .authenticator(RefreshAuthenticator(baseUrl))
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
                }
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(RuppApiService::class.java)
    }

    private fun isPublic(request: Request): Boolean {
        val path = request.url.encodedPath
        return path.endsWith("/auth/login") || path.endsWith("/auth/refresh") || path.endsWith("/auth/register")
    }

    private inner class AuthHeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val token = session.accessToken
            if (isPublic(request) || token.isNullOrBlank()) return chain.proceed(request)
            return chain.proceed(
                request.newBuilder().header("Authorization", "Bearer $token").build()
            )
        }
    }

    private inner class RefreshAuthenticator(private val baseUrl: String) : Authenticator {
        private val plainClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        override fun authenticate(route: Route?, response: Response): Request? {
            if (isPublic(response.request)) return null
            // Only retry once per request.
            if (response.priorResponse != null) {
                session.notifySessionExpired()
                return null
            }
            synchronized(this@ApiClient) {
                val sentToken = response.request.header("Authorization")?.removePrefix("Bearer ")
                val current = session.accessToken
                // Another call already refreshed the token: just retry with the new one.
                if (!current.isNullOrBlank() && current != sentToken) {
                    return response.request.newBuilder().header("Authorization", "Bearer $current").build()
                }
                val refresh = session.refreshToken ?: run {
                    session.notifySessionExpired()
                    return null
                }
                val body = gson.toJson(RefreshTokenRequest(refresh))
                    .toRequestBody("application/json".toMediaType())
                val refreshCall = Request.Builder().url(baseUrl + "api/v1/auth/refresh").post(body).build()
                val newToken = runCatching {
                    plainClient.newCall(refreshCall).execute().use { r ->
                        if (!r.isSuccessful) return@use null
                        val json = gson.fromJson(r.body?.string(), JsonObject::class.java)
                        val data = json?.getAsJsonObject("data") ?: return@use null
                        val access = data.get("accessToken")?.asString ?: return@use null
                        val newRefresh = data.get("refreshToken")?.asString ?: refresh
                        session.saveAuth(
                            kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.AuthResponse(
                                accessToken = access,
                                refreshToken = newRefresh,
                                tokenType = "Bearer",
                                expiresIn = null,
                                user = null
                            )
                        )
                        access
                    }
                }.getOrNull()

                if (newToken == null) {
                    session.notifySessionExpired()
                    return null
                }
                return response.request.newBuilder().header("Authorization", "Bearer $newToken").build()
            }
        }
    }
}

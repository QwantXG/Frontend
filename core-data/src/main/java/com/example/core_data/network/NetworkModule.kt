package com.example.core_data.network

import com.example.core_data.api.TTravelsApi
import com.example.core_data.model.AuthResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response as RetrofitResponse
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


interface AuthTokensProvider {
    fun getAccessToken(): String?
}

class AuthorizationInterceptor(
    private val tokensProvider: AuthTokensProvider?,
    private val preemptiveRefreshCall: (suspend (String) -> RetrofitResponse<AuthResponse>)? = null
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        if (original.url.encodedPath.contains("/account/refresh")) {
            return chain.proceed(original)
        }
        val tokensStore = tokensProvider as? TokensStore
        val currentTokens = tokensStore?.currentTokens()
        if (preemptiveRefreshCall != null && currentTokens != null) {
            val now = System.currentTimeMillis()
            if (!currentTokens.isRefreshExpired(now) && currentTokens.shouldRefreshAccess(nowMillis = now)) {
                val refreshed = runBlocking {
                    val response = preemptiveRefreshCall.invoke(currentTokens.refreshToken)
                    if (response.isSuccessful) response.body()?.toAuthTokens(nowMillis = now) else null
                }
                if (refreshed != null) {
                    tokensStore.saveTokens(refreshed)
                }
            }
        }
        val token = tokensProvider?.getAccessToken()
        val request = if (!token.isNullOrBlank()) {
            original.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            original
        }
        return chain.proceed(request)
    }
}

object NetworkDefaults {
    const val DEFAULT_BASE_URL = "http://localhost:8081/api/v1/"
    const val ACCESS_REFRESH_THRESHOLD_MS = 60_000L
}

fun defaultJson(): Json = Json {
    ignoreUnknownKeys = true
}

object ApiFactory {
    fun createApi(
        baseUrl: String = NetworkDefaults.DEFAULT_BASE_URL,
        tokensProvider: AuthTokensProvider? = null,
        json: Json = defaultJson(),
        authenticator: Authenticator? = null,
        preemptiveRefreshCall: (suspend (String) -> RetrofitResponse<AuthResponse>)? = null,
        configureClient: (OkHttpClient.Builder.() -> Unit)? = null
    ): TTravelsApi {
        val clientBuilder = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(AuthorizationInterceptor(tokensProvider, preemptiveRefreshCall))
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    redactHeader("Authorization")
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )

        authenticator?.let { clientBuilder.authenticator(it) }
        configureClient?.invoke(clientBuilder)

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        return retrofit.create(TTravelsApi::class.java)
    }
}

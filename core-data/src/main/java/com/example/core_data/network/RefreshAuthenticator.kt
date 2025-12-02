package com.example.core_data.network

import com.example.core_data.model.AuthResponse
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class RefreshAuthenticator(
    private val tokensStore: TokensStore,
    private val refreshCall: suspend (String) -> retrofit2.Response<AuthResponse>
    ) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null
        if (response.request.url.encodedPath.contains("/account/refresh")) return null

        val currentTokens = tokensStore.currentTokens() ?: return null
        if (currentTokens.isRefreshExpired()) {
            tokensStore.clear()
            return null
        }

        val currentRefresh = currentTokens.refreshToken
        if (currentRefresh.isBlank()) return null

        val newTokens = runBlocking {
            val refreshResponse = refreshCall(currentRefresh)
            if (refreshResponse.isSuccessful) {
                refreshResponse.body()?.toAuthTokens()
            } else {
                null
            }
        } ?: return null

        tokensStore.saveTokens(newTokens)
        val newAccess = newTokens.accessToken
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccess")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var current = response.priorResponse
        while (current != null) {
            count++
            current = current.priorResponse
        }
        return count
    }
}

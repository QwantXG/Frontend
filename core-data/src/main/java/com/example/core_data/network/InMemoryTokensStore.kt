package com.example.core_data.network

class InMemoryTokensStore(initialTokens: AuthTokens? = null) : TokensStore {

    @Volatile
    private var cached: AuthTokens? = initialTokens

    override fun currentTokens(): AuthTokens? = cached

    override fun getAccessToken(): String? = cached?.accessToken

    override fun saveTokens(tokens: AuthTokens) {
        cached = tokens
    }

    override fun updateAccessToken(accessToken: String?) {
        if (accessToken.isNullOrBlank()) return
        val current = cached ?: return
        cached = current.copy(accessToken = accessToken)
    }

    override fun clear() {
        cached = null
    }
}

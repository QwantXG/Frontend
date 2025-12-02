package com.example.core_data.network

interface TokensStore : AuthTokensProvider {
    fun currentTokens(): AuthTokens?
    fun saveTokens(tokens: AuthTokens)
    fun updateAccessToken(accessToken: String?)
    fun clear()
}

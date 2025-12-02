package com.example.core_data.network

import android.content.Context
import android.content.SharedPreferences

class PersistentTokensStore private constructor(
    context: Context,
    prefsName: String
) : TokensStore {

    companion object {
        const val DEFAULT_PREFS_NAME = "core_data_tokens"
        fun create(context: Context?, prefsName: String = DEFAULT_PREFS_NAME): PersistentTokensStore =
            PersistentTokensStore(context.applicationContext, prefsName)
    }

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    @Volatile
    private var cached: AuthTokens? = readFromPrefs()

    override fun currentTokens(): AuthTokens? = cached

    override fun getAccessToken(): String? = cached?.accessToken

    @Synchronized
    override fun saveTokens(tokens: AuthTokens) {
        cached = tokens
        prefs.edit()
            .putString(Keys.ACCESS, tokens.accessToken)
            .putString(Keys.REFRESH, tokens.refreshToken)
            .applyLongOrRemove(Keys.ACCESS_EXP, tokens.accessExpiresAtMillis)
            .applyLongOrRemove(Keys.REFRESH_EXP, tokens.refreshExpiresAtMillis)
            .apply()
    }

    @Synchronized
    override fun updateAccessToken(accessToken: String?) {
        if (accessToken.isNullOrBlank()) return
        val current = cached ?: return
        saveTokens(current.copy(accessToken = accessToken))
    }

    @Synchronized
    override fun clear() {
        cached = null
        prefs.edit().clear().apply()
    }

    private fun readFromPrefs(): AuthTokens? {
        val access = prefs.getString(Keys.ACCESS, null) ?: return null
        val refresh = prefs.getString(Keys.REFRESH, null) ?: return null
        val accessExp = prefs.getLong(Keys.ACCESS_EXP, -1L).takeIf { it >= 0 }
        val refreshExp = prefs.getLong(Keys.REFRESH_EXP, -1L).takeIf { it >= 0 }
        return AuthTokens(
            accessToken = access,
            refreshToken = refresh,
            accessExpiresAtMillis = accessExp,
            refreshExpiresAtMillis = refreshExp
        )
    }

    private object Keys {
        const val ACCESS = "access"
        const val REFRESH = "refresh"
        const val ACCESS_EXP = "access_exp"
        const val REFRESH_EXP = "refresh_exp"
    }

    private fun SharedPreferences.Editor.applyLongOrRemove(
        key: String,
        value: Long?
    ): SharedPreferences.Editor {
        return if (value != null) putLong(key, value) else remove(key)
    }
}

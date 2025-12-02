package com.example.myapplication

import android.content.Context
import com.example.core_data.network.NetworkDefaults
import com.example.core_data.network.PersistentTokensStore
import com.example.core_data.repository.TTravelsBackend

object BackendProvider {
    @Volatile
    private var backend: TTravelsBackend? = null

    fun get(context: Context): TTravelsBackend {
        val appContext = context.applicationContext
        return backend ?: synchronized(this) {
            backend ?: TTravelsBackend.create(
                baseUrl = BuildConfig.BASE_URL.ifBlank {
                    NetworkDefaults.DEFAULT_BASE_URL
                },
                tokensStore = PersistentTokensStore.create(appContext),
                json = com.example.core_data.network.defaultJson()
            ).also { backend = it }
        }
    }


    fun get(): TTravelsBackend {
        return MyApplication.backend
    }
}
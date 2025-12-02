package com.example.myapplication

import android.app.Application
import com.example.core_data.network.PersistentTokensStore
import com.example.core_data.network.defaultJson
import com.example.core_data.repository.TTravelsBackend

class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
        lateinit var backend: TTravelsBackend
    }

    override fun onCreate() {
        super.onCreate()
        instance = this


        backend = TTravelsBackend.create(
            baseUrl = BuildConfig.BASE_URL,
            tokensStore = PersistentTokensStore.create(this),
            json = defaultJson()
        )
    }
}
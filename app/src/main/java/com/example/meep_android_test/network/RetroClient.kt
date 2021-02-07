package com.example.meep_android_test.network

import com.example.meep_android_test.network.api.ResourcesApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetroClient {

    private val baseUrl = "https://apidev.meep.me"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val resourcesApi: ResourcesApi
        get() = retrofit.create(ResourcesApi::class.java)
}
package com.example.harmony.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




object RetrofitClient {
    private const val BASE_URL = "https://discoveryprovider.audius.co/"

    val instance: AudiusApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(AudiusApiService::class.java)
    }
}
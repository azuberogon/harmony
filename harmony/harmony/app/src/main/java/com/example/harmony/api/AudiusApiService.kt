package com.example.harmony.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AudiusApiService {
    @GET("v1/tracks")
    fun getTracks(): Call<List<Track>>
    @GET("v1/tracks/trending")
    fun getTrendingTracks(): Call<Track>

    @GET("v1/tracks/search")
    fun searchTrack(@Query("query") query: String): Call<TracksResponse>
}
package com.example.harmony.api

data class TracksResponse(
    val data: List<Track>
)

data class Track(
    val title: String,
    val artist: String
)

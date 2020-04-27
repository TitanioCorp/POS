package com.titaniocorp.pos.api

import com.titaniocorp.pos.BuildConfig

data class SearchMovieRequest(
    val search: String,
    val page: Int,
    val apiKey: String = ""
)
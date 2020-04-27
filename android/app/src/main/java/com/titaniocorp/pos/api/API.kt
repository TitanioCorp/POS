package com.titaniocorp.pos.api

import com.titaniocorp.pos.api.response.SearchMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Define todos los servicios que se vayan a consumir
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
interface API {
    /**
     * Servicio para buscar varias películas
     * @author Juan Ortiz
     * @date 10/09/2019
     * */
    @GET("3/search/movie")
    suspend fun searchMovies(@Query("api_key") apiKey: String,
                             @Query("query") query: String,
                             @Query("page") page: String) : Response<SearchMoviesResponse>
}
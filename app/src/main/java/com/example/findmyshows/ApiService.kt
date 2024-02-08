package com.example.findmyshows

import com.example.findmyshows.dataclasses.QueryKeywords
import com.example.findmyshows.dataclasses.QuerySeason
import com.example.findmyshows.dataclasses.QueryShow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Obtém os programas populares da TV
    @GET("tv/popular")
    fun getPopular(@Query("api_key") apiKey: String): Call<com.example.findmyshows.dataclasses.Query>

    // Obtém os programas de TV com base na query fornecida
    @GET("search/tv")
    fun getShows(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<com.example.findmyshows.dataclasses.Query>

    // Obtém os detalhes de um programa de TV específico
    @GET("tv/{showId}")
    fun getShowDetails(
        @Path("showId") showId: Int,
        @Query("api_key") apiKey: String
    ): Call<QueryShow>

    // Obtém as palavras-chave associadas a um programa de TV específico
    @GET("tv/{showId}/keywords")
    fun getKeywords(
        @Path("showId") showId: Int,
        @Query("api_key") apiKey: String
    ): Call<QueryKeywords>

    // Obtém os episódios de uma temporada específica de um programa de TV
    @GET("tv/{showId}/season/{seasonId}")
    fun getEpisodes(
        @Path("showId") showId: Int,
        @Path("seasonId") seasonId: Int,
        @Query("api_key") apiKey: String
    ): Call<QuerySeason>
}

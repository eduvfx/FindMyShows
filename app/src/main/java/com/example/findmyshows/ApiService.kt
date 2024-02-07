package com.example.findmyshows

import com.example.findmyshows.dataclasses.QueryKeywords
import com.example.findmyshows.dataclasses.QueryShow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("tv/popular")
    fun getPopular(@Query("api_key") apiKey: String): Call<com.example.findmyshows.dataclasses.Query>

    @GET("search/tv")
    fun getShows(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<com.example.findmyshows.dataclasses.Query>

    @GET("tv/{showId}")
    fun getShowDetails(
        @Path("showId") showId: Int,
        @Query("api_key") apiKey: String
    ): Call<QueryShow>

    @GET("tv/{showId}/keywords")
    fun getKeywords(
        @Path("showId") showId: Int,
        @Query("api_key") apiKey: String
    ): Call<QueryKeywords>
}
package com.example.findmyshows

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface ApiService {

    @GET("tv/popular")
    fun getPost(@Query("api_key") apiKey: String): Call<Popular>
}
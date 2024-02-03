package com.example.findmyshows

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShowsAdapter
    private val key = "39a3c712614c598a6d5ca7a7c35a3ab1"
    val popularCall = apiService.getPopular(apiKey = key)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchView = findViewById<SearchView>(R.id.searchbar)
        searchView.onActionViewExpanded()
        searchView.clearFocus()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission here
                if (query.isNullOrBlank()) {
                    getQuery(popularCall)
                } else {
                    val call = apiService.getShows(apiKey = key, query = query)
                    getQuery(call)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        // Clear focus from SearchView to lower the keyboard

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ShowsAdapter(emptyList())
        recyclerView.adapter = adapter

        // Make a sample network request using Retrofit
        getQuery(popularCall)
    }

    private fun getQuery(call: Call<Query>) {
        call.enqueue(object : retrofit2.Callback<Query> {
            override fun onResponse(call: retrofit2.Call<Query>, response: retrofit2.Response<Query>) {
                if (response.isSuccessful) {
                    val apiResponse: List<Result>? = response.body()?.results
                    listShows(apiResponse)
                    // Handle the API response
                } else {
                    // Handle unsuccessful response
                    val errorMessage: String = when (response.code()) {
                        404 -> "Resource not found"
                        401 -> "Unauthorized"
                        // Add more cases for other HTTP status codes as needed
                        else -> "Unknown error"
                    }
                    Log.e("ApiError", "HTTP Status Code: ${response.code()}, Message: $errorMessage")
                }
            }

            override fun onFailure(call: retrofit2.Call<Query>, t: Throwable) {
                // Handle network error
                Log.e("NetworkError", "Network request failed", t)
            }
        })
    }

    private fun listShows(results: List<Result>?) {
        // Update the adapter with the new list of items
        results?.let {
            adapter = ShowsAdapter(it)
            recyclerView.adapter = adapter
        }
    }

    companion object RetrofitClient {
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService: ApiService = retrofit.create(ApiService::class.java)
    }
}

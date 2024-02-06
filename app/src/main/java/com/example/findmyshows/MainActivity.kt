package com.example.findmyshows

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
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
    private lateinit var noResults: TextView
    private lateinit var progress: ProgressBar
    private val key = "39a3c712614c598a6d5ca7a7c35a3ab1"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noResults = findViewById(R.id.noResults)
        progress = findViewById(R.id.progress)
        val searchView = findViewById<SearchView>(R.id.searchbar)
        searchView.onActionViewExpanded()
        searchView.clearFocus()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission here
                if (!query.isNullOrBlank()) {
                    val call = apiService.getShows(apiKey = key, query = query)
                    getQuery(call)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    val popularCall = apiService.getPopular(apiKey = key)
                    getQuery(popularCall)
                }
                return true
            }
        })
        // Clear focus from SearchView to lower the keyboard

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ShowsAdapter(emptyList())
        recyclerView.adapter = adapter

        // Make a sample network request using Retrofit
        val popularCall = apiService.getPopular(apiKey = key)
        getQuery(popularCall)
    }

    private fun getQuery(call: Call<Query>) {
        recyclerView.visibility = View.INVISIBLE
        progress.visibility = View.VISIBLE
        call.enqueue(object : retrofit2.Callback<Query> {
            override fun onResponse(call: Call<Query>, response: retrofit2.Response<Query>) {
                if (response.isSuccessful) {
                    if (response.body()?.total_results!! > 0) {
                        noResults.visibility = View.INVISIBLE
                        recyclerView.visibility = View.VISIBLE
                        val apiResponse: List<Result>? = response.body()?.results
                        listShows(apiResponse)
                    } else {
                        noResults.visibility = View.VISIBLE
                        recyclerView.visibility = View.INVISIBLE
                    }
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

            override fun onFailure(call: Call<Query>, t: Throwable) {
                // Handle network error
                Log.e("NetworkError", "Network request failed", t)
            }
        })
        recyclerView.visibility = View.VISIBLE
        progress.visibility = View.INVISIBLE
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

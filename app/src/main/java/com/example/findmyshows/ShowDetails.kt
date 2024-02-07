package com.example.findmyshows

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import com.example.findmyshows.dataclasses.QueryKeywords
import com.example.findmyshows.dataclasses.QueryShow
import com.example.findmyshows.dataclasses.ResultKeywords
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class ShowDetails : AppCompatActivity() {

    private lateinit var flowLayout: Flow
    private lateinit var constraintLayout: ConstraintLayout
    private val key = "39a3c712614c598a6d5ca7a7c35a3ab1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_details)

        flowLayout = findViewById(R.id.keywords)
        constraintLayout = findViewById(R.id.constraintLayout)
        val back = findViewById<ImageView>(R.id.toolbar_icon)
        back.setOnClickListener {
            finish()
        }
        val receivedInt = intent.getIntExtra("id", 0)
        val detailsCall = apiService.getShowDetails(showId = receivedInt, apiKey = key)
        getDetails(detailsCall)
        val keywordsCall = apiService.getKeywords(showId = receivedInt, apiKey = key)
        getKeywords(keywordsCall)
    }

    private fun getKeywords(call: Call<QueryKeywords>) {
        call.enqueue(object : retrofit2.Callback<QueryKeywords> {
            override fun onResponse(
                call: Call<QueryKeywords>,
                response: retrofit2.Response<QueryKeywords>
            ) {
                if (response.isSuccessful) {
                    val apiResponse: List<ResultKeywords>? = response.body()?.results
                    if (apiResponse != null) {
                        for (item in apiResponse) {
                            val textView = TextView(this@ShowDetails)
                            textView.text = item.name
                            textView.id = View.generateViewId() // Generate a unique ID for the TextView
                            textView.setPadding(8, 8, 8, 8)
                            textView.setBackgroundResource(R.drawable.keyword_background) // Set your background drawable here
                            // Add the TextView to the parent ConstraintLayout
                            constraintLayout.addView(textView)
                            // Apply constraints to position the TextView within the Flow
                            val params = ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                ConstraintLayout.LayoutParams.WRAP_CONTENT
                            )
                            textView.layoutParams = params
                            flowLayout.addView(textView)
                        }
                    }
                } else {
                    // Handle unsuccessful response
                    val errorMessage: String = when (response.code()) {
                        404 -> "Resource not found"
                        401 -> "Unauthorized"
                        // Add more cases for other HTTP status codes as needed
                        else -> "Unknown error"
                    }
                    Log.e(
                        "ApiError",
                        "HTTP Status Code: ${response.code()}, Message: $errorMessage"
                    )
                }
            }

            override fun onFailure(call: Call<QueryKeywords>, t: Throwable) {
                // Handle network error
                Log.e("NetworkError", "Network request failed", t)
            }
        })
    }

    private fun getDetails(call: Call<QueryShow>) {
        call.enqueue(object : retrofit2.Callback<QueryShow> {
            override fun onResponse(
                call: Call<QueryShow>,
                response: retrofit2.Response<QueryShow>
            ) {
                if (response.isSuccessful) {
                    val apiResponse: QueryShow? = response.body()
                    val score = apiResponse?.vote_average?.times(10)?.roundToInt()
                    val progressbar = findViewById<ProgressBar>(R.id.progressBar)
                    if (score != null) {
                        progressbar.progress = score
                        progressbar.progressTintList = when {
                            score >= 70 -> ColorStateList.valueOf(Color.parseColor("#21d07a"))// Green
                            score >= 40 -> ColorStateList.valueOf(Color.parseColor("#d2d531")) // Yellow
                            else -> ColorStateList.valueOf(Color.parseColor("#db2360")) // Red
                        }
                    }
                    findViewById<TextView>(R.id.toolbar_title).text = apiResponse?.name
                    findViewById<TextView>(R.id.title).text = apiResponse?.name
                    findViewById<TextView>(R.id.textProgress).text = score.toString()
                    findViewById<TextView>(R.id.tagline).text = apiResponse?.tagline
                    findViewById<TextView>(R.id.overview).text = apiResponse?.overview
                    if (apiResponse != null) {
                        var genres = ""
                        for (genre in apiResponse.genres) {
                            genres += if (genres == "") {
                                genre.name
                            } else {
                                ", ${genre.name}"
                            }
                        }
                        findViewById<TextView>(R.id.genres).text = genres
                    }
                    val background = findViewById<ImageView>(R.id.background)
                    if (apiResponse?.backdrop_path != null) {
                        Picasso.get()
                            .load("https://image.tmdb.org/t/p/w500/" + apiResponse.backdrop_path)
                            .into(background)
                    } else if (apiResponse?.poster_path != null) {
                        Picasso.get()
                            .load("https://image.tmdb.org/t/p/w500/" + apiResponse.poster_path)
                            .into(background)
                    } else {
                        background.setImageResource(R.drawable.nophoto)
                    }
                    val poster = findViewById<ImageView>(R.id.poster)
                    if (apiResponse?.poster_path != null) {
                        Picasso.get()
                            .load("https://image.tmdb.org/t/p/w500/" + apiResponse.poster_path)
                            .into(poster)
                    } else {
                        background.setImageResource(R.drawable.nophoto)
                    }
                } else {
                    // Handle unsuccessful response
                    val errorMessage: String = when (response.code()) {
                        404 -> "Resource not found"
                        401 -> "Unauthorized"
                        // Add more cases for other HTTP status codes as needed
                        else -> "Unknown error"
                    }
                    Log.e(
                        "ApiError",
                        "HTTP Status Code: ${response.code()}, Message: $errorMessage"
                    )
                }
            }

            override fun onFailure(call: Call<QueryShow>, t: Throwable) {
                // Handle network error
                Log.e("NetworkError", "Network request failed", t)
            }
        })
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
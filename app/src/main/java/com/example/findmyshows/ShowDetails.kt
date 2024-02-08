package com.example.findmyshows

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.findmyshows.dataclasses.*
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class ShowDetails : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EpisodeAdapter
    private lateinit var flowLayout: Flow
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var dropdown: Spinner
    private lateinit var progressBarDetails: ProgressBar
    private lateinit var progressBar: ProgressBar
    private lateinit var background: ImageView
    private lateinit var poster: ImageView
    private lateinit var detailsScroll: NestedScrollView
    private var receivedInt: Int = 0

    private val key = "39a3c712614c598a6d5ca7a7c35a3ab1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_details)

        initializeViews()
        formatRecyclerView()

        val back = findViewById<ImageView>(R.id.toolbar_icon)
        back.setOnClickListener {
            finish()
        }

        receivedInt = intent.getIntExtra("id", 0)

        showLoading()

        val detailsCall = apiService.getShowDetails(showId = receivedInt, apiKey = key)
        getDetails(detailsCall)

        val keywordsCall = apiService.getKeywords(showId = receivedInt, apiKey = key)
        getKeywords(keywordsCall)
    }

    private fun showLoading() {
        progressBarDetails.visibility = View.VISIBLE
        detailsScroll.visibility = View.GONE
    }

    private fun formatRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager?.canScrollVertically()
        recyclerView.layoutManager?.canScrollHorizontally()
    }

    private fun initializeViews() {
        background = findViewById(R.id.background)
        poster = findViewById(R.id.poster)
        progressBar = findViewById(R.id.progressBar)
        progressBarDetails = findViewById(R.id.progressBarDetails)
        detailsScroll = findViewById(R.id.detailsScroll)
        flowLayout = findViewById(R.id.keywords)
        constraintLayout = findViewById(R.id.constraintLayout)
        recyclerView = findViewById(R.id.episodes)
    }

    private fun getEpisodes(call: Call<QuerySeason>) {
        call.enqueue(object : retrofit2.Callback<QuerySeason> {
            override fun onResponse(
                call: Call<QuerySeason>,
                response: retrofit2.Response<QuerySeason>
            ) {
                if (response.isSuccessful) {
                    val apiResponse: List<Episode>? = response.body()?.episodes
                    listEpisodes(apiResponse)
                } else {
                    handleErrorResponse(response)
                }
            }

            override fun onFailure(call: Call<QuerySeason>, t: Throwable) {
                Log.e("NetworkError", "Network request failed", t)
            }
        })
    }

    private fun getKeywords(call: Call<QueryKeywords>) {
        call.enqueue(object : retrofit2.Callback<QueryKeywords> {
            override fun onResponse(
                call: Call<QueryKeywords>,
                response: retrofit2.Response<QueryKeywords>
            ) {
                if (response.isSuccessful) {
                    val apiResponse: List<ResultKeywords>? = response.body()?.results
                    apiResponse?.let { displayKeywords(it) }
                } else {
                    handleErrorResponse(response)
                }
            }

            override fun onFailure(call: Call<QueryKeywords>, t: Throwable) {
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
                    apiResponse?.let { displayShowDetails(it) }
                } else {
                    handleErrorResponse(response)
                }
            }

            override fun onFailure(call: Call<QueryShow>, t: Throwable) {
                Log.e("NetworkError", "Network request failed", t)
            }
        })
    }

    private fun listEpisodes(episodes: List<Episode>?) {
        episodes?.let {
            adapter = EpisodeAdapter(it)
            recyclerView.adapter = adapter
        }
    }

    private fun handleErrorResponse(response: retrofit2.Response<*>) {
        val errorMessage: String = when (response.code()) {
            404 -> "Resource not found"
            401 -> "Unauthorized"
            else -> "Unknown error"
        }
        Log.e("ApiError", "HTTP Status Code: ${response.code()}, Message: $errorMessage")
    }

    private fun displayKeywords(keywords: List<ResultKeywords>) {
        for (item in keywords) {
            val textView = TextView(this@ShowDetails)
            textView.text = item.name
            textView.id = View.generateViewId()
            textView.setPadding(8, 8, 8, 8)
            textView.setBackgroundResource(R.drawable.keyword_background)
            constraintLayout.addView(textView)
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            textView.layoutParams = params
            flowLayout.addView(textView)
        }
    }

    private fun displayShowDetails(apiResponse: QueryShow) {
        val score = apiResponse.vote_average.times(10).roundToInt()

        setProgressBarColor(score)

        setupTextViews(apiResponse, score)

        setGenresText(apiResponse.genres)

        setupDropDown(apiResponse)

        setImages(apiResponse)

        hideLoading()
    }

    private fun setupDropDown(apiResponse: QueryShow) {
        val mutableList: MutableList<String> = mutableListOf()
        for (season in apiResponse.seasons) {
            mutableList.add(season.name)
        }
        val stringsArray = mutableList.toTypedArray()
        val adapter =
            ArrayAdapter(this@ShowDetails, android.R.layout.simple_spinner_item, stringsArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropdown = findViewById(R.id.dropdown)
        dropdown.adapter = adapter

        setDropdownListener(apiResponse)
    }

    private fun setDropdownListener(apiResponse: QueryShow) {
        val seasonMap = apiResponse.seasons.associateBy({ it.name }, { it.season_number })
        dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                val seasonNumber = seasonMap[selectedItem]
                val episodesCall = seasonNumber?.let {
                    apiService.getEpisodes(
                        showId = receivedInt,
                        seasonId = it,
                        apiKey = key
                    )
                }
                episodesCall?.let { getEpisodes(it) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setupTextViews(
        apiResponse: QueryShow,
        score: Int
    ) {
        findViewById<TextView>(R.id.toolbar_title).text = apiResponse.name
        findViewById<TextView>(R.id.title).text = apiResponse.name
        findViewById<TextView>(R.id.textProgress).text = score.toString()
        findViewById<TextView>(R.id.tagline).text = apiResponse.tagline
        findViewById<TextView>(R.id.overview).text = apiResponse.overview
    }

    private fun hideLoading() {
        progressBarDetails.visibility = View.GONE
        detailsScroll.visibility = View.VISIBLE
    }

    private fun setImages(apiResponse: QueryShow) {
        if (apiResponse.backdrop_path != null) {
            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500/" + apiResponse.backdrop_path)
                .into(background)
        } else if (apiResponse.poster_path != null) {
            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500/" + apiResponse.poster_path)
                .into(background)
        } else {
            background.setImageResource(R.drawable.nophoto)
            poster.setImageResource(R.drawable.nophoto)
        }

        if (apiResponse.poster_path != null) {
            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500/" + apiResponse.poster_path)
                .into(poster)
        } else {
            poster.setImageResource(R.drawable.nophoto)
        }
    }

    private fun setGenresText(genreList: List<Genre>) {
        var genres = ""
        for (genre in genreList) {
            genres += if (genres == "") {
                genre.name
            } else {
                ", ${genre.name}"
            }
        }
        findViewById<TextView>(R.id.genres).text = genres
    }

    private fun setProgressBarColor(score: Int) {
        score.let {
            progressBar.progress = score
            progressBar.progressTintList = when {
                score >= 70 -> ColorStateList.valueOf(Color.parseColor("#21d07a"))
                score >= 40 -> ColorStateList.valueOf(Color.parseColor("#d2d531"))
                else -> ColorStateList.valueOf(Color.parseColor("#db2360"))
            }
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

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
import com.example.findmyshows.dataclasses.Query
import com.example.findmyshows.dataclasses.Result
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShowsAdapter
    private lateinit var noResults: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchView: SearchView

    // Chave da API
    private val key = "39a3c712614c598a6d5ca7a7c35a3ab1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa as views
        initializeViews()
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Define o listener da barra de pesquisa
        setSearchListener()

        // Obtém os programas populares
        val popularCall = apiService.getPopular(apiKey = key)
        getShows(popularCall)
    }

    // Inicializa as views da atividade
    private fun initializeViews() {
        noResults = findViewById(R.id.noResults)
        progressBar = findViewById(R.id.progressBar)
        searchView = findViewById(R.id.searchbar)
        recyclerView = findViewById(R.id.recyclerView)
    }

    // Define o listener da barra de pesquisa
    private fun setSearchListener() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    showLoading()
                    val call = apiService.getShows(apiKey = key, query = query)
                    getShows(call)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    showLoading()
                    val popularCall = apiService.getPopular(apiKey = key)
                    getShows(popularCall)
                }
                return true
            }
        })
    }

    // Mostra a barra de progresso
    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        noResults.visibility = View.GONE
    }

    // Obtém os programas com base na chamada da API
    private fun getShows(call: Call<Query>) {
        call.enqueue(object : retrofit2.Callback<Query> {
            override fun onResponse(call: Call<Query>, response: retrofit2.Response<Query>) {
                if (response.isSuccessful) {
                    if (response.body()?.total_results!! > 0) {
                        val apiResponse: List<Result>? = response.body()?.results
                        listShows(apiResponse)
                    } else {
                        hideLoading()
                    }
                } else {
                    // Manipula erros da resposta
                    val errorMessage: String = when (response.code()) {
                        404 -> "Recurso não encontrado"
                        401 -> "Não autorizado"
                        else -> "Erro desconhecido"
                    }
                    Log.e(
                        "ApiError",
                        "Código de status HTTP: ${response.code()}, Mensagem: $errorMessage"
                    )
                }
            }

            override fun onFailure(call: Call<Query>, t: Throwable) {
                // Manipula falhas de rede
                Log.e("NetworkError", "Falha na requisição de rede", t)
            }
        })
    }

    // Esconde a barra de progresso e mostra a mensagem de nenhum resultado
    private fun hideLoading() {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
        noResults.visibility = View.VISIBLE
    }

    // Lista os programas na RecyclerView
    private fun listShows(results: List<Result>?) {
        results?.let {
            adapter = ShowsAdapter(it)
            recyclerView.adapter = adapter
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    companion object RetrofitClient {
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        // Configuração do cliente Retrofit
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Criação do serviço da API
        val apiService: ApiService = retrofit.create(ApiService::class.java)
    }
}

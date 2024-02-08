package com.example.findmyshows

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.findmyshows.dataclasses.Result
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class ShowsAdapter(private val itemList: List<Result>) :
    RecyclerView.Adapter<ShowsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shows_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    // Retorna o número total de itens na lista
    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Vincula os dados do item ao layout da visualização
        fun bind(item: Result) {
            val context = itemView.context

            itemView.findViewById<TextView>(R.id.textTitle).text = item.name
            itemView.findViewById<TextView>(R.id.textDate).text =
                if (item.first_air_date.isNotEmpty()) formatDate(item.first_air_date) else ""

            setupProgressBar(item)

            setPicture(item)

            setOnClickListener(context, item)
        }

        // Define um listener de clique para a visualização do item
        private fun setOnClickListener(
            context: Context,
            item: Result
        ) {
            val layout = itemView.findViewById<RelativeLayout>(R.id.layout)
            layout.setOnClickListener {
                val intent = Intent(context, ShowDetails::class.java)
                intent.putExtra("id", item.id)
                context.startActivity(intent)
            }
        }

        // Define a imagem do programa de TV
        private fun setPicture(item: Result) {
            val picture = itemView.findViewById<ImageView>(R.id.picture)
            if (item.poster_path != null) {
                Picasso.get().load("https://image.tmdb.org/t/p/w500/${item.poster_path}")
                    .into(picture)
            } else {
                picture.setImageResource(R.drawable.nophoto)
            }
        }

        // Configura a barra de progresso para mostrar a classificação do programa
        private fun setupProgressBar(item: Result) {
            val score = (item.vote_average * 10).toInt()
            itemView.findViewById<TextView>(R.id.textProgress).text = score.toString()

            val progressbar = itemView.findViewById<ProgressBar>(R.id.progressBar)
            progressbar.progress = score
            progressbar.progressTintList = when {
                score >= 70 -> ColorStateList.valueOf(Color.parseColor("#21d07a")) // Verde
                score >= 40 -> ColorStateList.valueOf(Color.parseColor("#d2d531")) // Amarelo
                else -> ColorStateList.valueOf(Color.parseColor("#db2360")) // Vermelho
            }
        }

        // Formata a data do programa de TV para exibição
        private fun formatDate(inputDate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd '/' MMMM '/' yyyy", Locale("en", "US"))
            val date = inputFormat.parse(inputDate)
            return outputFormat.format(date!!)
        }
    }
}

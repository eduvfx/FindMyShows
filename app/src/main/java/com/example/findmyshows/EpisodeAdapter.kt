package com.example.findmyshows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.findmyshows.dataclasses.Episode
import java.text.SimpleDateFormat
import java.util.*
import com.squareup.picasso.Picasso

class EpisodeAdapter(private val itemList: List<Episode>) :
    RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.episode_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        // Retorna o número total de itens na lista de episódios
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Componentes visuais do layout do episódio
        private val picture: ImageView = itemView.findViewById(R.id.episodeFrame)
        private val episodeTitle: TextView = itemView.findViewById(R.id.episodeTitle)
        private val episodeText: TextView = itemView.findViewById(R.id.episodeText)

        // Vincula os dados do episódio aos componentes visuais
        fun bind(item: Episode) {
            episodeTitle.text = item.name
            setDate(item)
            setPicture(item.still_path)
        }

        // Define a data do episódio formatada
        private fun setDate(item: Episode) {
            episodeText.text = if (item.air_date != null) {
                val formattedDate = formatDate(item.air_date)
                "$formattedDate | ${item.runtime} min"
            } else {
                ""
            }
        }

        // Define a imagem do episódio
        private fun setPicture(path: String) {
            if (path != null) {
                Picasso.get().load("https://image.tmdb.org/t/p/w500/$path")
                    .into(picture)
            } else {
                // Define uma imagem padrão caso não haja imagem disponível
                picture.setImageResource(R.drawable.nophoto)
            }
        }

        // Formata a data do episódio
        private fun formatDate(inputDate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd '/' MMMM '/' yyyy", Locale("en", "US"))
            val date = inputFormat.parse(inputDate)
            return outputFormat.format(date!!)
        }
    }
}

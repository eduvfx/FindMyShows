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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.episode_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val picture: ImageView = itemView.findViewById(R.id.episodeFrame)
        private val episodeTitle: TextView = itemView.findViewById(R.id.episodeTitle)
        private val episodeText: TextView = itemView.findViewById(R.id.episodeText)

        fun bind(item: Episode) {
            episodeTitle.text = item.name
            setDate(item)
            setPicture(item.still_path)
        }

        private fun setDate(item: Episode) {
            episodeText.text = if (item.air_date != null) {
                val formattedDate = formatDate(item.air_date)
                "$formattedDate | ${item.runtime} min"
            } else {
                ""
            }
        }

        private fun setPicture(path: String) {
            if (path != null) {
                Picasso.get().load("https://image.tmdb.org/t/p/w500/$path")
                    .into(picture)
            } else {
                picture.setImageResource(R.drawable.nophoto)
            }
        }

        private fun formatDate(inputDate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "PT"))
            val date = inputFormat.parse(inputDate)
            return outputFormat.format(date!!)
        }
    }
}
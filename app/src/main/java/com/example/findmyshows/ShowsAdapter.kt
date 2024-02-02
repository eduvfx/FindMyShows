package com.example.findmyshows

import android.content.res.ColorStateList
import android.graphics.Color
import kotlin.math.roundToInt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


class ShowsAdapter(private val itemList: List<Result>) : RecyclerView.Adapter<ShowsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shows_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        // Bind data to the UI elements in the item layout
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Declare UI elements in the item layout and bind them here
        fun bind(item: Result) {
            // Bind data to the UI elements
            itemView.findViewById<TextView>(R.id.textTitle).text = item.name
            itemView.findViewById<TextView>(R.id.textDate).text = formatDate(item.first_air_date)
            val score = (item.vote_average * 10).roundToInt()
            itemView.findViewById<TextView>(R.id.textProgress).text = score.toString()
            val progressbar = itemView.findViewById<ProgressBar>(R.id.progressBar)
            progressbar.progress = score
            progressbar.progressTintList = when {
                score >= 70 -> ColorStateList.valueOf(Color.parseColor("#21d07a"))// Green
                score >= 40 -> ColorStateList.valueOf(Color.parseColor("#d2d531")) // Yellow
                else -> ColorStateList.valueOf(Color.parseColor("#db2360")) // Red
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
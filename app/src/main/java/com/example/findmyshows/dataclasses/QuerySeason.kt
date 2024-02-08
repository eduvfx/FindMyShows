package com.example.findmyshows.dataclasses

data class QuerySeason (
    val _id: String,
    val air_date: String,
    val episodes: List<Episode>,
    val name: String,
    val overview: String,
    val id: Int,
    val poster_path: String,
    val season_number: Int,
    val vote_average: Double
)
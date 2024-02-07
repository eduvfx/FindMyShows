package com.example.findmyshows.dataclasses

import com.example.findmyshows.dataclasses.CreatedBy
import com.example.findmyshows.dataclasses.Genre
import com.example.findmyshows.dataclasses.LastEpisodeToAir
import com.example.findmyshows.dataclasses.Networks
import com.example.findmyshows.dataclasses.NextEpisodeToAir
import com.example.findmyshows.dataclasses.ProductionCompanies
import com.example.findmyshows.dataclasses.ProductionCountries
import com.example.findmyshows.dataclasses.Season
import com.example.findmyshows.dataclasses.SpokenLanguages

data class QueryShow (
    val adult: Boolean,
    val backdrop_path: String,
    val created_by: List<CreatedBy>,
    val episode_run_time: List<Int>,
    val first_air_date: String,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val in_production: Boolean,
    val languages: List<String>,
    val last_air_date: String,
    val last_episode_to_air: LastEpisodeToAir,
    val name: String,
    val next_episode_to_air: NextEpisodeToAir,
    val networks: List<Networks>,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompanies>,
    val production_countries: List<ProductionCountries>,
    val seasons: List<Season>,
    val spoken_languages: List<SpokenLanguages>,
    val status: String,
    val tagline: String,
    val type: String,
    val vote_average: Double,
    val vote_count: Int
)
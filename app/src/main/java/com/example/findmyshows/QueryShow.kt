package com.example.findmyshows

import com.example.findmyshows.queryclasses.CreatedBy
import com.example.findmyshows.queryclasses.Genre
import com.example.findmyshows.queryclasses.LastEpisodeToAir
import com.example.findmyshows.queryclasses.Networks
import com.example.findmyshows.queryclasses.NextEpisodeToAir
import com.example.findmyshows.queryclasses.ProductionCompanies
import com.example.findmyshows.queryclasses.ProductionCountries
import com.example.findmyshows.queryclasses.Season
import com.example.findmyshows.queryclasses.SpokenLanguages

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
package com.example.findmyshows.dataclasses

data class Query (
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)
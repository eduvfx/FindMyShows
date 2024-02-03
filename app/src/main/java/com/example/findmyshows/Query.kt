package com.example.findmyshows

data class Query (
    val page: Int,
    val results: List<Result>,
    val totalPages: Int,
    val totalResults: Int
)
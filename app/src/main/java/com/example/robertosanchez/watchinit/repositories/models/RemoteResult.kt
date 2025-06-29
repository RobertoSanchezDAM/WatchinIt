package com.example.robertosanchez.watchinit.repositories.models

data class RemoteResult(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)
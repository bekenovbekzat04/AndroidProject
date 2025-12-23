package com.example.finalproject.core.domain.model


import  com.example.finalproject.core.network.dto.AnimeDto

data class Anime(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String
)

fun AnimeDto.toDomain() = Anime(
    id = id,
    title = title,
    description = synopsis ?: "No description available",
    imageUrl = images.jpg.imageUrl
)
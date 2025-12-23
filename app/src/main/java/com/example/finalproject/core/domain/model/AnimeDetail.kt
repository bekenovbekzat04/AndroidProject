package com.example.finalproject.core.domain.model


import com.example.finalproject.core.network.dto.AnimeDetailDto

data class AnimeDetail(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val score: Double?,
    val episodes: Int?,
    val status: String?,
    val duration: String?,
    val genres: List<String>,
    val studios: List<String>,
    val rating: String?,
    val year: Int?,
    val background: String?,
    val trailerUrl: String?
)

fun AnimeDetailDto.toDomain() = AnimeDetail(
    id = id,
    title = title,
    description = synopsis ?: "No description available",
    imageUrl = images.jpg.imageUrl,
    score = score,
    episodes = episodes,
    status = status,
    duration = duration,
    genres = genres?.map { it.name } ?: emptyList(),
    studios = studios?.map { it.name } ?: emptyList(),
    rating = rating,
    year = year,
    background = background,
    trailerUrl = trailer?.url ?: trailer?.embedUrl
)

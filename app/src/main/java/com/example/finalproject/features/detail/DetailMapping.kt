package com.example.finalproject.features.detail


import com.example.finalproject.core.domain.model.Anime
import com.example.finalproject.core.domain.model.AnimeDetail

fun AnimeDetail.toAnime(): Anime = Anime(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl
)

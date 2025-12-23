package com.example.finalproject.features.profile


import com.example.finalproject.core.domain.model.Anime
import com.example.finalproject.features.search.SearchItem

fun Anime.toItem() = SearchItem(
    id = id,
    title = title,
    imageUrl = imageUrl,
    description = description
)

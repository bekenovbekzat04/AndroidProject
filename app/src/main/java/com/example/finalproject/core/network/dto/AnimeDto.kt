package com.example.finalproject.core.network.dto


import com.google.gson.annotations.SerializedName

data class AnimeDto(
    @SerializedName("mal_id")
    val id: Int,

    val title: String,

    val synopsis: String?,

    val images: ImagesDto
)
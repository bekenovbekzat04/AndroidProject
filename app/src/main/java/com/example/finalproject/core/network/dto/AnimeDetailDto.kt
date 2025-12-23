package com.example.finalproject.core.network.dto


import com.google.gson.annotations.SerializedName

data class AnimeDetailDto(
    @SerializedName("mal_id")
    val id: Int,
    val title: String,
    val synopsis: String?,
    val images: ImagesDto,
    val score: Double?,
    val episodes: Int?,
    val status: String?,
    val duration: String?,
    val genres: List<NamedResourceDto>?,
    val studios: List<NamedResourceDto>?,
    val rating: String?,
    val year: Int?,
    val background: String?,
    val trailer: TrailerDto?
)

data class NamedResourceDto(
    @SerializedName("mal_id")
    val id: Int,
    val name: String
)

data class TrailerDto(
    @SerializedName("youtube_id")
    val youtubeId: String?,
    val url: String?,
    @SerializedName("embed_url")
    val embedUrl: String?
)

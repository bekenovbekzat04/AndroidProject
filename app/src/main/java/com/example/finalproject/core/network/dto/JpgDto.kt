package com.example.finalproject.core.network.dto

import com.google.gson.annotations.SerializedName

data class JpgDto(
    @SerializedName("image_url")
    val imageUrl: String
)
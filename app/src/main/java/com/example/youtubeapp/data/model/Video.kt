package com.example.youtubeapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "videos")
data class Video(
    @PrimaryKey
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("thumbnail")
    val thumbnailUrl: String,

    @SerializedName("channel")
    val channelName: String,

    @SerializedName("views")
    val viewCount: String,

    @SerializedName("duration")
    val duration: String,

    @SerializedName("publishedAt")
    val publishedAt: String,

    @SerializedName("channelThumbnail")
    val channelThumbnail: String = "",

    val isFavorite: Boolean = false,

    val savedAt: Long = System.currentTimeMillis()
)

data class VideoResponse(
    @SerializedName("videos")
    val videos: List<Video>,

    @SerializedName("nextPage")
    val nextPage: String? = null
)
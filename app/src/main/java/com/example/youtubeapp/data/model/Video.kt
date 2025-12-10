package com.example.youtubeapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "videos")
data class Video(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val channelName: String,
    val channelId: String,
    val viewCount: String,
    val likeCount: String,
    val duration: String,
    val publishedAt: String,
    val channelThumbnail: String = "",
    val isFavorite: Boolean = false,
    val savedAt: Long = System.currentTimeMillis()
)

data class YouTubeSearchResponse(
    @SerializedName("items")
    val items: List<SearchItem>,
    @SerializedName("nextPageToken")
    val nextPageToken: String? = null,
    @SerializedName("pageInfo")
    val pageInfo: PageInfo
)

data class SearchItem(
    @SerializedName("id")
    val id: VideoId,
    @SerializedName("snippet")
    val snippet: Snippet
)

data class VideoId(
    @SerializedName("videoId")
    val videoId: String
)

data class Snippet(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("channelId")
    val channelId: String,
    @SerializedName("channelTitle")
    val channelTitle: String,
    @SerializedName("thumbnails")
    val thumbnails: Thumbnails
)

data class Thumbnails(
    @SerializedName("default")
    val default: Thumbnail? = null,
    @SerializedName("medium")
    val medium: Thumbnail? = null,
    @SerializedName("high")
    val high: Thumbnail
)

data class Thumbnail(
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int? = null,
    @SerializedName("height")
    val height: Int? = null
)

data class PageInfo(
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("resultsPerPage")
    val resultsPerPage: Int
)

data class YouTubeVideoDetailsResponse(
    @SerializedName("items")
    val items: List<VideoDetailsItem>
)

data class VideoDetailsItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("snippet")
    val snippet: Snippet,
    @SerializedName("contentDetails")
    val contentDetails: ContentDetails,
    @SerializedName("statistics")
    val statistics: Statistics
)

data class ContentDetails(
    @SerializedName("duration")
    val duration: String // ISO 8601 format: PT15M51S
)

data class Statistics(
    @SerializedName("viewCount")
    val viewCount: String,
    @SerializedName("likeCount")
    val likeCount: String? = null,
    @SerializedName("commentCount")
    val commentCount: String? = null
)

data class YouTubeChannelResponse(
    @SerializedName("items")
    val items: List<ChannelItem>
)

data class ChannelItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("snippet")
    val snippet: ChannelSnippet
)

data class ChannelSnippet(
    @SerializedName("title")
    val title: String,
    @SerializedName("thumbnails")
    val thumbnails: Thumbnails
)
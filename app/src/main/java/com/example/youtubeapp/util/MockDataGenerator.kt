package com.example.youtubeapp.util

import com.example.youtubeapp.data.model.Video

object MockDataGenerator {

    fun generateMockVideos(count: Int = 20): List<Video> {
        return (1..count).map { index ->
            Video(
                id = "video_$index",
                title = "Amazing Video Title #$index - Learn Android Development",
                description = "This is a detailed description for video #$index. " +
                        "Learn how to build amazing Android applications with Kotlin, " +
                        "Coroutines, Retrofit, Room Database and MVVM architecture.",
                thumbnailUrl = "https://picsum.photos/480/270?random=$index",
                channelName = "Tech Channel ${(index % 5) + 1}",
                channelId = "channel_${(index % 5) + 1}",
                viewCount = "${(index * 1234)}K",
                likeCount = "${(index * 123)}",
                duration = "${(index % 10) + 5}:${(index % 60).toString().padStart(2, '0')}",
                publishedAt = "${(index % 30) + 1} days ago",
                channelThumbnail = "https://picsum.photos/100/100?random=${index + 100}",
                isFavorite = false
            )
        }
    }
}
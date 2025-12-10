package com.example.youtubeapp.util

import com.example.youtubeapp.data.model.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object YouTubeUtils {


    fun convertToVideo(
        searchItem: SearchItem,
        videoDetails: VideoDetailsItem?,
        channelThumbnail: String = ""
    ): Video {
        val snippet = searchItem.snippet

        return Video(
            id = searchItem.id.videoId,
            title = snippet.title,
            description = snippet.description,
            thumbnailUrl = snippet.thumbnails.high.url,
            channelName = snippet.channelTitle,
            channelId = snippet.channelId,
            viewCount = formatViewCount(videoDetails?.statistics?.viewCount ?: "0"),
            likeCount = videoDetails?.statistics?.likeCount ?: "0",
            duration = parseDuration(videoDetails?.contentDetails?.duration ?: "PT0S"),
            publishedAt = formatPublishedDate(snippet.publishedAt),
            channelThumbnail = channelThumbnail,
            isFavorite = false,
            savedAt = System.currentTimeMillis()
        )
    }


    fun parseDuration(duration: String): String {
        try {
            val pattern = "PT(\\d+H)?(\\d+M)?(\\d+S)?".toRegex()
            val matchResult = pattern.find(duration) ?: return "0:00"

            val hours = matchResult.groups[1]?.value?.removeSuffix("H")?.toIntOrNull() ?: 0
            val minutes = matchResult.groups[2]?.value?.removeSuffix("M")?.toIntOrNull() ?: 0
            val seconds = matchResult.groups[3]?.value?.removeSuffix("S")?.toIntOrNull() ?: 0

            return when {
                hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, seconds)
                else -> String.format("%d:%02d", minutes, seconds)
            }
        } catch (e: Exception) {
            return "0:00"
        }
    }


    fun formatViewCount(viewCount: String): String {
        return try {
            val count = viewCount.toLongOrNull() ?: 0
            when {
                count >= 1_000_000_000 -> String.format("%.1fB", count / 1_000_000_000.0)
                count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
                count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
                else -> count.toString()
            }
        } catch (e: Exception) {
            "0"
        }
    }


    fun formatPublishedDate(publishedAt: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val date = sdf.parse(publishedAt) ?: return publishedAt

            val now = System.currentTimeMillis()
            val diff = now - date.time

            val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
            val hours = TimeUnit.MILLISECONDS.toHours(diff)
            val days = TimeUnit.MILLISECONDS.toDays(diff)
            val weeks = days / 7
            val months = days / 30
            val years = days / 365

            when {
                years > 0 -> "$years ${if (years == 1L) "year" else "years"} ago"
                months > 0 -> "$months ${if (months == 1L) "month" else "months"} ago"
                weeks > 0 -> "$weeks ${if (weeks == 1L) "week" else "weeks"} ago"
                days > 0 -> "$days ${if (days == 1L) "day" else "days"} ago"
                hours > 0 -> "$hours ${if (hours == 1L) "hour" else "hours"} ago"
                minutes > 0 -> "$minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
                else -> "$seconds ${if (seconds == 1L) "second" else "seconds"} ago"
            }
        } catch (e: Exception) {
            publishedAt
        }
    }


    fun getVideoIds(items: List<SearchItem>): String {
        return items.joinToString(",") { it.id.videoId }
    }


    fun getChannelIds(items: List<SearchItem>): String {
        return items.map { it.snippet.channelId }.distinct().joinToString(",")
    }
}
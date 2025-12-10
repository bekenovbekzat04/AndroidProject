package com.example.youtubeapp.data.remote

import com.example.youtubeapp.data.model.YouTubeChannelResponse
import com.example.youtubeapp.data.model.YouTubeSearchResponse
import com.example.youtubeapp.data.model.YouTubeVideoDetailsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {


    @GET("search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 20,
        @Query("pageToken") pageToken: String? = null,
        @Query("key") apiKey: String
    ): Response<YouTubeSearchResponse>


    @GET("search")
    suspend fun getPopularVideos(
        @Query("part") part: String = "snippet",
        @Query("chart") chart: String = "mostPopular",
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 20,
        @Query("pageToken") pageToken: String? = null,
        @Query("regionCode") regionCode: String = "US",
        @Query("key") apiKey: String
    ): Response<YouTubeSearchResponse>


    @GET("search")
    suspend fun getVideosByCategory(
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("videoCategoryId") categoryId: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("pageToken") pageToken: String? = null,
        @Query("key") apiKey: String
    ): Response<YouTubeSearchResponse>


    @GET("videos")
    suspend fun getVideoDetails(
        @Query("part") part: String = "snippet,contentDetails,statistics",
        @Query("id") videoIds: String, // Comma-separated video IDs
        @Query("key") apiKey: String
    ): Response<YouTubeVideoDetailsResponse>


    @GET("channels")
    suspend fun getChannelDetails(
        @Query("part") part: String = "snippet",
        @Query("id") channelIds: String, // Comma-separated channel IDs
        @Query("key") apiKey: String
    ): Response<YouTubeChannelResponse>
}
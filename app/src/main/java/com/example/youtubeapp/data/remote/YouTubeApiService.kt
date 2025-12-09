package com.example.youtubeapp.data.remote

import com.example.youtubeapp.data.model.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {

    @GET("videos/trending")
    suspend fun getTrendingVideos(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<VideoResponse>

    @GET("videos/search")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<VideoResponse>

    @GET("videos/category")
    suspend fun getVideosByCategory(
        @Query("category") category: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<VideoResponse>
}
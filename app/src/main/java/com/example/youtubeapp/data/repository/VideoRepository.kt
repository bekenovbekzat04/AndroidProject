package com.example.youtubeapp.data.repository

import androidx.lifecycle.LiveData
import com.example.youtubeapp.data.local.PreferencesManager
import com.example.youtubeapp.data.local.VideoDao
import com.example.youtubeapp.data.model.Video
import com.example.youtubeapp.data.remote.RetrofitClient
import com.example.youtubeapp.data.remote.YouTubeApiService
import com.example.youtubeapp.util.YouTubeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoRepository(
    private val apiService: YouTubeApiService,
    private val videoDao: VideoDao,
    private val preferencesManager: PreferencesManager
) {

    private val apiKey = RetrofitClient.API_KEY

    fun getAllVideosFromDb(): LiveData<List<Video>> {
        return videoDao.getAllVideos()
    }

    fun getFavoriteVideos(): LiveData<List<Video>> {
        return videoDao.getFavoriteVideos()
    }


    suspend fun fetchTrendingVideos(forceRefresh: Boolean = false): Result<List<Video>> {
        return withContext(Dispatchers.IO) {
            try {
                val cachedVideos = videoDao.getVideoCount()
                val lastSync = preferencesManager.lastSyncTime
                val shouldFetch = forceRefresh ||
                        cachedVideos == 0 ||
                        System.currentTimeMillis() - lastSync > 300000 // 5 min

                if (shouldFetch) {
                    val searchResponse = apiService.searchVideos(
                        query = "trending",
                        maxResults = 20,
                        apiKey = apiKey
                    )

                    if (searchResponse.isSuccessful && searchResponse.body() != null) {
                        val searchItems = searchResponse.body()!!.items

                        val videoIds = YouTubeUtils.getVideoIds(searchItems)

                        val videoDetailsResponse = apiService.getVideoDetails(
                            videoIds = videoIds,
                            apiKey = apiKey
                        )

                        val videoDetailsMap = videoDetailsResponse.body()?.items
                            ?.associateBy { it.id } ?: emptyMap()

                        val channelIds = YouTubeUtils.getChannelIds(searchItems)

                        val channelResponse = apiService.getChannelDetails(
                            channelIds = channelIds,
                            apiKey = apiKey
                        )

                        val channelThumbnailMap = channelResponse.body()?.items
                            ?.associateBy({ it.id }, { it.snippet.thumbnails.high.url })
                            ?: emptyMap()

                        val videos = searchItems.map { searchItem ->
                            val videoDetails = videoDetailsMap[searchItem.id.videoId]
                            val channelThumbnail = channelThumbnailMap[searchItem.snippet.channelId] ?: ""
                            YouTubeUtils.convertToVideo(searchItem, videoDetails, channelThumbnail)
                        }

                        videoDao.clearAllVideos()
                        videoDao.insertVideos(videos)
                        preferencesManager.lastSyncTime = System.currentTimeMillis()

                        Result.success(videos)
                    } else {
                        val cachedData = videoDao.getAllVideos().value ?: emptyList()
                        if (cachedData.isNotEmpty()) {
                            Result.success(cachedData)
                        } else {
                            Result.failure(Exception("Failed to load videos: ${searchResponse.message()}"))
                        }
                    }
                } else {
                    val cachedData = videoDao.getAllVideos().value ?: emptyList()
                    Result.success(cachedData)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val cachedData = videoDao.getAllVideos().value ?: emptyList()
                if (cachedData.isNotEmpty()) {
                    Result.success(cachedData)
                } else {
                    Result.failure(e)
                }
            }
        }
    }


    suspend fun searchVideos(query: String): Result<List<Video>> {
        return withContext(Dispatchers.IO) {
            try {
                val searchResponse = apiService.searchVideos(
                    query = query,
                    maxResults = 20,
                    apiKey = apiKey
                )

                if (searchResponse.isSuccessful && searchResponse.body() != null) {
                    val searchItems = searchResponse.body()!!.items

                    val videoIds = YouTubeUtils.getVideoIds(searchItems)
                    val videoDetailsResponse = apiService.getVideoDetails(
                        videoIds = videoIds,
                        apiKey = apiKey
                    )

                    val videoDetailsMap = videoDetailsResponse.body()?.items
                        ?.associateBy { it.id } ?: emptyMap()

                    val channelIds = YouTubeUtils.getChannelIds(searchItems)
                    val channelResponse = apiService.getChannelDetails(
                        channelIds = channelIds,
                        apiKey = apiKey
                    )

                    val channelThumbnailMap = channelResponse.body()?.items
                        ?.associateBy({ it.id }, { it.snippet.thumbnails.high.url })
                        ?: emptyMap()

                    val videos = searchItems.map { searchItem ->
                        val videoDetails = videoDetailsMap[searchItem.id.videoId]
                        val channelThumbnail = channelThumbnailMap[searchItem.snippet.channelId] ?: ""
                        YouTubeUtils.convertToVideo(searchItem, videoDetails, channelThumbnail)
                    }

                    Result.success(videos)
                } else {
                    Result.failure(Exception("Search failed: ${searchResponse.message()}"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }


    suspend fun getVideosByCategory(categoryId: String): Result<List<Video>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getVideosByCategory(
                    categoryId = categoryId,
                    maxResults = 20,
                    apiKey = apiKey
                )

                if (response.isSuccessful && response.body() != null) {
                    val searchItems = response.body()!!.items

                    val videoIds = YouTubeUtils.getVideoIds(searchItems)
                    val videoDetailsResponse = apiService.getVideoDetails(
                        videoIds = videoIds,
                        apiKey = apiKey
                    )

                    val videoDetailsMap = videoDetailsResponse.body()?.items
                        ?.associateBy { it.id } ?: emptyMap()

                    val videos = searchItems.map { searchItem ->
                        val videoDetails = videoDetailsMap[searchItem.id.videoId]
                        YouTubeUtils.convertToVideo(searchItem, videoDetails)
                    }

                    Result.success(videos)
                } else {
                    Result.failure(Exception("Failed to load category videos"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun toggleFavorite(videoId: String, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            videoDao.updateFavoriteStatus(videoId, isFavorite)
        }
    }

    suspend fun getVideoById(videoId: String): Video? {
        return withContext(Dispatchers.IO) {
            videoDao.getVideoById(videoId)
        }
    }

    suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            videoDao.clearAllVideos()
        }
    }
}
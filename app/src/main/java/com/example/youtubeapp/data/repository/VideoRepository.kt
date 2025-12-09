package com.example.youtubeapp.data.repository

import androidx.lifecycle.LiveData
import com.example.youtubeapp.data.local.PreferencesManager
import com.example.youtubeapp.data.local.VideoDao
import com.example.youtubeapp.data.model.Video
import com.example.youtubeapp.data.model.VideoResponse
import com.example.youtubeapp.data.remote.YouTubeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoRepository(
    private val apiService: YouTubeApiService,
    private val videoDao: VideoDao,
    private val preferencesManager: PreferencesManager
) {

    // Get videos from local database
    fun getAllVideosFromDb(): LiveData<List<Video>> {
        return videoDao.getAllVideos()
    }

    fun getFavoriteVideos(): LiveData<List<Video>> {
        return videoDao.getFavoriteVideos()
    }

    // Fetch trending videos from API with caching
    suspend fun fetchTrendingVideos(forceRefresh: Boolean = false): Result<List<Video>> {
        return withContext(Dispatchers.IO) {
            try {
                // Check if we should use cached data
                val cachedVideos = videoDao.getVideoCount()
                val lastSync = preferencesManager.lastSyncTime
                val shouldFetch = forceRefresh ||
                        cachedVideos == 0 ||
                        System.currentTimeMillis() - lastSync > 300000 // 5 min

                if (shouldFetch) {
                    // Fetch from network
                    val response = apiService.getTrendingVideos()

                    if (response.isSuccessful && response.body() != null) {
                        val videos = response.body()!!.videos

                        // Save to database
                        videoDao.insertVideos(videos)
                        preferencesManager.lastSyncTime = System.currentTimeMillis()

                        Result.success(videos)
                    } else {
                        // If network fails, return cached data
                        val cachedData = videoDao.getAllVideos().value ?: emptyList()
                        if (cachedData.isNotEmpty()) {
                            Result.success(cachedData)
                        } else {
                            Result.failure(Exception("No data available"))
                        }
                    }
                } else {
                    // Use cached data
                    val cachedData = videoDao.getAllVideos().value ?: emptyList()
                    Result.success(cachedData)
                }
            } catch (e: Exception) {
                // On error, try to return cached data
                val cachedData = videoDao.getAllVideos().value ?: emptyList()
                if (cachedData.isNotEmpty()) {
                    Result.success(cachedData)
                } else {
                    Result.failure(e)
                }
            }
        }
    }

    // Search videos
    suspend fun searchVideos(query: String): Result<List<Video>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.searchVideos(query)

                if (response.isSuccessful && response.body() != null) {
                    val videos = response.body()!!.videos
                    Result.success(videos)
                } else {
                    Result.failure(Exception("Search failed"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // Toggle favorite status
    suspend fun toggleFavorite(videoId: String, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            videoDao.updateFavoriteStatus(videoId, isFavorite)
        }
    }

    // Get video by ID
    suspend fun getVideoById(videoId: String): Video? {
        return withContext(Dispatchers.IO) {
            videoDao.getVideoById(videoId)
        }
    }

    // Clear cache
    suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            videoDao.clearAllVideos()
        }
    }
}
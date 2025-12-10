package com.example.youtubeapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.youtubeapp.data.local.AppDatabase
import com.example.youtubeapp.data.local.PreferencesManager
import com.example.youtubeapp.data.model.Video
import com.example.youtubeapp.data.remote.RetrofitClient
import com.example.youtubeapp.data.repository.VideoRepository
import com.example.youtubeapp.util.MockDataGenerator
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VideoRepository

    private val _videos = MutableLiveData<List<Video>>()
    val videos: LiveData<List<Video>> = _videos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    val favoriteVideos: LiveData<List<Video>>
    val cachedVideos: LiveData<List<Video>>

    init {
        val database = AppDatabase.getDatabase(application)
        val preferencesManager = PreferencesManager(application)
        repository = VideoRepository(
            RetrofitClient.apiService,
            database.videoDao(),
            preferencesManager
        )

        favoriteVideos = repository.getFavoriteVideos()
        cachedVideos = repository.getAllVideosFromDb()

        // Load videos on init
        loadTrendingVideos()
    }

    fun loadTrendingVideos(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

//            val result = repository.fetchTrendingVideos(forceRefresh)
//
//            result.onSuccess { videoList ->
//                _videos.value = videoList
//                _isLoading.value = false
//            }.onFailure { exception ->
//                _error.value = exception.message ?: "Unknown error occurred"
//                _isLoading.value = false
//            }
            val mockVideos = MockDataGenerator.generateMockVideos(15)
            _videos.value = mockVideos
            _isLoading.value = false
        }
    }

    fun searchVideos(query: String) {
        if (query.isBlank()) {
            loadTrendingVideos()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.searchVideos(query)

            result.onSuccess { videoList ->
                _videos.value = videoList
                _isLoading.value = false
            }.onFailure { exception ->
                _error.value = exception.message ?: "Search failed"
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(video: Video) {
        viewModelScope.launch {
            repository.toggleFavorite(video.id, !video.isFavorite)
        }
    }

    fun refresh() {
        loadTrendingVideos(forceRefresh = true)
    }

    fun clearError() {
        _error.value = null
    }
}
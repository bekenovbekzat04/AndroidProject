package com.example.finalproject.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.core.common.Result
import com.example.finalproject.core.domain.model.Anime
import com.example.finalproject.core.domain.usecase.AnimeSection
import com.example.finalproject.core.domain.usecase.GetTopAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopAnime: GetTopAnimeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun loadHomeData() {
        viewModelScope.launch {
            _state.value = HomeState.Loading

            val trending = when (val result = getTopAnime(AnimeSection.Trending)) {
                is Result.Success -> result.data
                is Result.Error -> {
                    _state.value = HomeState.Error(result.error.message)
                    return@launch
                }
            }

            val popular = when (val result = getTopAnime(AnimeSection.Popular)) {
                is Result.Success -> result.data
                is Result.Error -> {
                    _state.value = HomeState.Error(result.error.message)
                    return@launch
                }
            }

            val topRated = when (val result = getTopAnime(AnimeSection.TopRated)) {
                is Result.Success -> result.data
                is Result.Error -> {
                    _state.value = HomeState.Error(result.error.message)
                    return@launch
                }
            }

            val lastWatched = trending.firstOrNull()

            _state.value = HomeState.Success(
                lastWatched = lastWatched,
                trending = trending,
                popular = popular,
                topRated = topRated
            )
        }
    }
}

sealed class HomeState {
    object Loading : HomeState()
    data class Success(
        val lastWatched: Anime?,
        val trending: List<Anime>,
        val popular: List<Anime>,
        val topRated: List<Anime>
    ) : HomeState()
    data class Error(val message: String) : HomeState()
}

sealed class HomeSection {
    data class Banner(val anime: Anime) : HomeSection()
    data class Carousel(val title: String, val animeList: List<Anime>) : HomeSection()
}

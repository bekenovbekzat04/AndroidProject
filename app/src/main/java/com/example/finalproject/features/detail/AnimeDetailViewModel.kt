package com.example.finalproject.features.detail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.core.common.Result
import com.example.finalproject.core.domain.model.AnimeDetail
import com.example.finalproject.core.domain.usecase.GetAnimeDetailUseCase
import com.example.finalproject.core.domain.usecase.IsAnimeSavedUseCase
import com.example.finalproject.core.domain.usecase.SaveAnimeUseCase
import com.example.finalproject.core.domain.usecase.RemoveSavedAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    private val getAnimeDetail: GetAnimeDetailUseCase,
    private val saveAnimeUseCase: SaveAnimeUseCase,
    private val removeSavedAnimeUseCase: RemoveSavedAnimeUseCase,
    private val isAnimeSavedUseCase: IsAnimeSavedUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AnimeDetailState>(AnimeDetailState.Idle)
    val state: StateFlow<AnimeDetailState> = _state.asStateFlow()

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()

    fun load(animeId: Int) {
        _state.value = AnimeDetailState.Loading

        viewModelScope.launch {
            when (val result = getAnimeDetail(animeId)) {
                is Result.Success -> {
                    _state.value = AnimeDetailState.Success(result.data)
                    updateSavedState(result.data.id)
                }
                is Result.Error -> _state.value = AnimeDetailState.Error(result.error.message)
            }
        }
    }

    fun toggleSave(detail: AnimeDetail) {
        viewModelScope.launch {
            if (_isSaved.value) {
                when (removeSavedAnimeUseCase(detail.id)) {
                    is Result.Success -> _isSaved.value = false
                    is Result.Error -> Unit
                }
            } else {
                when (saveAnimeUseCase(detail.toAnime())) {
                    is Result.Success -> _isSaved.value = true
                    is Result.Error -> Unit
                }
            }
        }
    }

    private suspend fun updateSavedState(id: Int) {
        when (val result = isAnimeSavedUseCase(id)) {
            is Result.Success -> _isSaved.value = result.data
            is Result.Error -> _isSaved.value = false
        }
    }
}

sealed class AnimeDetailState {
    object Idle : AnimeDetailState()
    object Loading : AnimeDetailState()
    data class Success(val detail: AnimeDetail) : AnimeDetailState()
    data class Error(val message: String) : AnimeDetailState()
}

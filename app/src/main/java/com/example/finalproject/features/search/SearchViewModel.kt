package com.example.finalproject.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.core.common.Result
import com.example.finalproject.core.domain.model.Anime
import com.example.finalproject.core.domain.usecase.SearchAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val searchAnimeUseCase: SearchAnimeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<DiscoverState>(DiscoverState.Idle)
    val state: StateFlow<DiscoverState> = _state.asStateFlow()

    fun search(query: String) {
        viewModelScope.launch {
            _state.value = DiscoverState.Loading
            when (val result = searchAnimeUseCase(query, page = 1, limit = 20)) {
                is Result.Success -> _state.value = DiscoverState.Success(
                    items = result.data.map { anime -> anime.toItem() }
                )
                is Result.Error -> _state.value = DiscoverState.Error(result.error.message)
            }
        }
    }
}

data class SearchItem(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val description: String
)

fun Anime.toItem() = SearchItem(
    id = id,
    title = title,
    imageUrl = imageUrl,
    description = description
)

sealed class DiscoverState {
    object Idle : DiscoverState()
    object Loading : DiscoverState()
    data class Success(val items: List<SearchItem>) : DiscoverState()
    data class Error(val message: String) : DiscoverState()
}

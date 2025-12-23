package com.example.finalproject.features.profile


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.core.common.Result
import com.example.finalproject.core.domain.model.Anime
import com.example.finalproject.core.domain.usecase.GetSavedAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getSavedAnimeUseCase: GetSavedAnimeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    fun loadSaved() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            when (val result = getSavedAnimeUseCase()) {
                is Result.Success -> _state.value = ProfileState.Success(result.data)
                is Result.Error -> _state.value = ProfileState.Error(result.error.message)
            }
        }
    }
}

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val items: List<Anime>) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

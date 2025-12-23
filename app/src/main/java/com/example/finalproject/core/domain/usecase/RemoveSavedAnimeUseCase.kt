package com.example.finalproject.core.domain.usecase


import com.example.finalproject.core.common.Result
import com.example.finalproject.core.common.RootError
import com.example.finalproject.core.domain.AnimeRepository
import javax.inject.Inject

class RemoveSavedAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit, RootError> {
        return repository.removeSavedAnime(id)
    }
}

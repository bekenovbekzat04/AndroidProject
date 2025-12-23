package com.example.finalproject.core.domain.usecase


import com.example.finalproject.core.common.Result
import com.example.finalproject.core.common.RootError
import com.example.finalproject.core.domain.AnimeRepository
import javax.inject.Inject

class IsAnimeSavedUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(id: Int): Result<Boolean, RootError> {
        return repository.isSaved(id)
    }
}

package com.example.finalproject.core.domain.usecase


import com.example.finalproject.core.common.Result
import com.example.finalproject.core.common.RootError
import com.example.finalproject.core.domain.AnimeRepository
import com.example.finalproject.core.domain.model.AnimeDetail
import javax.inject.Inject

class GetAnimeDetailUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(id: Int): Result<AnimeDetail, RootError> {
        return repository.getAnimeDetail(id)
    }
}

package com.example.finalproject.core.domain.usecase


import com.example.finalproject.core.common.Result
import com.example.finalproject.core.common.RootError
import com.example.finalproject.core.domain.AnimeRepository
import com.example.finalproject.core.domain.model.Anime
import javax.inject.Inject

class GetSavedAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(): Result<List<Anime>, RootError> {
        return repository.getSavedAnime()
    }
}

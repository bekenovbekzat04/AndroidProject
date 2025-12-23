package com.example.finalproject.core.domain.usecase


import com.example.finalproject.core.common.Result
import com.example.finalproject.core.common.RootError
import com.example.finalproject.core.domain.AnimeRepository
import com.example.finalproject.core.domain.model.Anime
import javax.inject.Inject

class SaveAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(anime: Anime): Result<Unit, RootError> {
        return repository.saveAnime(anime)
    }
}

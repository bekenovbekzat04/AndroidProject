package com.example.finalproject.core.domain.usecase


import com.example.finalproject.core.common.Result
import com.example.finalproject.core.common.RootError
import com.example.finalproject.core.domain.AnimeRepository
import com.example.finalproject.core.domain.model.Anime
import javax.inject.Inject

enum class AnimeSection(val page: Int, val cacheKey: String) {
    Trending(page = 2, cacheKey = "trending"),
    Popular(page = 3, cacheKey = "popular"),
    TopRated(page = 1, cacheKey = "topRated")
}

class GetTopAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    companion object {
        private const val DEFAULT_PAGE_SIZE = 10
    }
    suspend operator fun invoke(
        section: AnimeSection,
        limit: Int = DEFAULT_PAGE_SIZE
    ): Result<List<Anime>, RootError> {
        return repository.getTopAnime(section, limit)
    }
}

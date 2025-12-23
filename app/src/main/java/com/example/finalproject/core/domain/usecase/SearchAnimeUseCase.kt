package com.example.finalproject.core.domain.usecase


import com.example.finalproject.core.common.Result
import com.example.finalproject.core.common.RootError
import com.example.finalproject.core.domain.AnimeRepository
import com.example.finalproject.core.domain.model.Anime
import javax.inject.Inject

class SearchAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(
        query: String,
        page: Int,
        limit: Int,
        type: String? = null,
        rating: String? = null,
        status: String? = null,
        orderBy: String? = null,
        sort: String? = null,
        sfw: Boolean? = null,
        unapproved: Boolean? = null
    ): Result<List<Anime>, RootError> {
        return repository.searchAnime(
            query = query,
            page = page,
            limit = limit,
            type = type,
            rating = rating,
            status = status,
            orderBy = orderBy,
            sort = sort,
            sfw = sfw,
            unapproved = unapproved
        )
    }
}

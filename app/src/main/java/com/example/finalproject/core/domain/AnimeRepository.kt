package com.example.finalproject.core.domain


import com.example.finalproject.core.common.Result
import com.example.finalproject.core.common.RootError
import com.example.finalproject.core.domain.model.Anime
import com.example.finalproject.core.domain.model.AnimeDetail
import com.example.finalproject.core.domain.usecase.AnimeSection

interface AnimeRepository {
    suspend fun getTopAnime(section: AnimeSection, limit: Int): Result<List<Anime>, RootError>
    suspend fun getAnimeDetail(id: Int): Result<AnimeDetail, RootError>
    suspend fun searchAnime(
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
    ): Result<List<Anime>, RootError>

    suspend fun getSavedAnime(): Result<List<Anime>, RootError>
    suspend fun saveAnime(anime: Anime): Result<Unit, RootError>
    suspend fun removeSavedAnime(id: Int): Result<Unit, RootError>
    suspend fun isSaved(id: Int): Result<Boolean, RootError>
}

package com.example.finalproject.core.data.remote


import com.example.finalproject.core.network.service.JikanService
import com.example.finalproject.core.network.dto.AnimeDetailDto
import com.example.finalproject.core.network.dto.AnimeDto
import com.example.finalproject.core.network.dto.JikanItemResponse
import javax.inject.Inject

class AnimeRemoteDataSource @Inject constructor(
    private val jikanService: JikanService
) {
    suspend fun getTopAnime(page: Int, limit: Int): List<AnimeDto> {
        val response = jikanService.getTopAnime(page, limit)
        if (response.isSuccessful) {
            return response.body()?.data.orEmpty()
        }
        throw NetworkException.HttpError(
            code = response.code(),
            message = response.message().ifBlank { "Failed to load anime" }
        )
    }

    suspend fun getAnimeDetail(id: Int): AnimeDetailDto {
        val response = jikanService.getAnimeDetail(id)
        if (response.isSuccessful) {
            val body: JikanItemResponse<AnimeDetailDto>? = response.body()
            if (body != null) {
                return body.data
            }
            throw NetworkException.Unexpected("Empty response body")
        }
        throw NetworkException.HttpError(
            code = response.code(),
            message = response.message().ifBlank { "Failed to load anime detail" }
        )
    }

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
    ): List<AnimeDto> {
        val response = jikanService.searchAnime(
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
        if (response.isSuccessful) {
            return response.body()?.data.orEmpty()
        }
        throw NetworkException.HttpError(
            code = response.code(),
            message = response.message().ifBlank { "Failed to search anime" }
        )
    }
}

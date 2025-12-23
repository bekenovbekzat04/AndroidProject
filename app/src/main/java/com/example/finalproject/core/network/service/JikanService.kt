package com.example.finalproject.core.network.service


import com.example.finalproject.core.network.dto.AnimeDetailDto
import com.example.finalproject.core.network.dto.JikanItemResponse
import com.example.finalproject.core.network.dto.JikanResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanService {
    @GET("anime")
    suspend fun getTopAnime(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<JikanResponse>

    @GET("anime/{id}/full")
    suspend fun getAnimeDetail(
        @Path("id") id: Int
    ): Response<JikanItemResponse<AnimeDetailDto>>

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("type") type: String? = null,
        @Query("rating") rating: String? = null,
        @Query("status") status: String? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("sort") sort: String? = null,
        @Query("sfw") sfw: Boolean? = null,
        @Query("unapproved") unapproved: Boolean? = null
    ): Response<JikanResponse>
}

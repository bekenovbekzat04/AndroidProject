package com.example.finalproject.core.data.repository

import com.example.finalproject.core.common.Result
import com.example.finalproject.core.common.RootError
import com.example.finalproject.core.data.local.AnimeLocalDataSource
import com.example.finalproject.core.data.local.toDomain
import com.example.finalproject.core.data.local.toEntity
import com.example.finalproject.core.data.remote.AnimeRemoteDataSource
import com.example.finalproject.core.data.remote.NetworkException
import com.example.finalproject.core.domain.AnimeRepository
import com.example.finalproject.core.domain.model.Anime
import com.example.finalproject.core.domain.model.AnimeDetail
import com.example.finalproject.core.domain.model.toDomain
import com.example.finalproject.core.domain.usecase.AnimeSection
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AnimeRepositoryImpl @Inject constructor(
    private val remoteDataSource: AnimeRemoteDataSource,
    private val localDataSource: AnimeLocalDataSource
) : AnimeRepository {

    companion object {
        private const val CACHE_DURATION_MS = 60 * 60 * 1000L // 1 hour cache
        private const val CATEGORY_SAVED = "saved"
    }

    override suspend fun getTopAnime(
        section: AnimeSection,
        limit: Int
    ): Result<List<Anime>, RootError> {
        return withContext(Dispatchers.IO) {
            val cached = localDataSource.getAnimeByCategory(section.cacheKey)

            if (isCacheValid(section.cacheKey) && cached.isNotEmpty()) {
                return@withContext Result.Success(cached.map { it.toDomain() })
            }

            when (val remoteResult = fetchRemoteAndCache(section, limit)) {
                is Result.Success -> remoteResult
                is Result.Error -> {
                    if (cached.isNotEmpty()) {
                        Result.Success(cached.map { it.toDomain() })
                    } else {
                        remoteResult
                    }
                }
            }
        }
    }

    override suspend fun getAnimeDetail(id: Int): Result<AnimeDetail, RootError> {
        return withContext(Dispatchers.IO) {
            try {
                val dto = remoteDataSource.getAnimeDetail(id)
                Result.Success(dto.toDomain())
            } catch (e: NetworkException.HttpError) {
                Result.Error(
                    RootError.Http(
                        code = e.code,
                        message = if (e.code == 404) {
                            "Anime not found"
                        } else {
                            e.message ?: "Failed to load anime"
                        }
                    )
                )
            } catch (e: NetworkException.Network) {
                Result.Error(RootError.Network(e.message ?: "Network error"))
            } catch (e: NetworkException.Unexpected) {
                Result.Error(RootError.Unexpected(e.message ?: "Unexpected error"))
            } catch (e: IOException) {
                Result.Error(RootError.Network(e.message ?: "Network error"))
            } catch (e: Exception) {
                Result.Error(RootError.Unexpected(e.message ?: "Unexpected error"))
            }
        }
    }

    override suspend fun getSavedAnime(): Result<List<Anime>, RootError> {
        return withContext(Dispatchers.IO) {
            val saved = localDataSource.getAnimeByCategory(CATEGORY_SAVED)
            Result.Success(saved.map { it.toDomain() })
        }
    }

    override suspend fun saveAnime(anime: Anime): Result<Unit, RootError> {
        return withContext(Dispatchers.IO) {
            try {
                localDataSource.saveAnime(anime.toEntity(CATEGORY_SAVED))
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(RootError.Unexpected(e.message ?: "Failed to save"))
            }
        }
    }

    override suspend fun removeSavedAnime(id: Int): Result<Unit, RootError> {
        return withContext(Dispatchers.IO) {
            try {
                localDataSource.deleteSavedById(CATEGORY_SAVED, id)
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(RootError.Unexpected(e.message ?: "Failed to remove saved"))
            }
        }
    }

    override suspend fun isSaved(id: Int): Result<Boolean, RootError> {
        return withContext(Dispatchers.IO) {
            try {
                val saved = localDataSource.getAnimeByCategory(CATEGORY_SAVED)
                Result.Success(saved.any { it.id == id })
            } catch (e: Exception) {
                Result.Error(RootError.Unexpected(e.message ?: "Failed to check saved"))
            }
        }
    }

    override suspend fun searchAnime(
        query: String,
        page: Int,
        limit: Int,
        type: String?,
        rating: String?,
        status: String?,
        orderBy: String?,
        sort: String?,
        sfw: Boolean?,
        unapproved: Boolean?
    ): Result<List<Anime>, RootError> {
        return withContext(Dispatchers.IO) {
            try {
                val dtoList = remoteDataSource.searchAnime(
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
                Result.Success(dtoList.map { it.toDomain() })
            } catch (e: NetworkException.HttpError) {
                Result.Error(
                    RootError.Http(
                        code = e.code,
                        message = e.message ?: "Failed to search anime"
                    )
                )
            } catch (e: NetworkException.Network) {
                Result.Error(RootError.Network(e.message ?: "Network error"))
            } catch (e: NetworkException.Unexpected) {
                Result.Error(RootError.Unexpected(e.message ?: "Unexpected error"))
            } catch (e: IOException) {
                Result.Error(RootError.Network(e.message ?: "Network error"))
            } catch (e: Exception) {
                Result.Error(RootError.Unexpected(e.message ?: "Unexpected error"))
            }
        }
    }

    private suspend fun fetchRemoteAndCache(
        section: AnimeSection,
        limit: Int
    ): Result<List<Anime>, RootError> {
        return try {
            val remoteAnime = remoteDataSource.getTopAnime(section.page, limit)
            val animeList = remoteAnime.map { it.toDomain() }
            if (animeList.isNotEmpty()) {
                cacheAnime(animeList, section.cacheKey)
            }
            Result.Success(animeList)
        } catch (e: NetworkException.HttpError) {
            Result.Error(
                RootError.Http(
                    code = e.code,
                    message = e.message ?: "Failed to load anime"
                )
            )
        } catch (e: NetworkException.Network) {
            Result.Error(RootError.Network(e.message ?: "Network error"))
        } catch (e: NetworkException.Unexpected) {
            Result.Error(RootError.Unexpected(e.message ?: "Unexpected error"))
        } catch (e: IOException) {
            Result.Error(RootError.Network(e.message ?: "Network error"))
        } catch (e: Exception) {
            Result.Error(RootError.Unexpected(e.message ?: "Unexpected error"))
        }
    }

    private suspend fun isCacheValid(category: String): Boolean {
        val cacheTime = localDataSource.getCacheTime(category) ?: return false
        val currentTime = System.currentTimeMillis()
        return (currentTime - cacheTime) < CACHE_DURATION_MS
    }

    private suspend fun cacheAnime(animeList: List<Anime>, category: String) {
        localDataSource.deleteAnimeByCategory(category)
        val entities = animeList.map { it.toEntity(category) }
        localDataSource.insertAnime(entities)
    }
}

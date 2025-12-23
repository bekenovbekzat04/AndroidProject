package com.example.finalproject.core.data.local


import javax.inject.Inject

class AnimeLocalDataSource @Inject constructor(
    private val animeDao: AnimeDao
) {
    suspend fun getAnimeByCategory(category: String): List<AnimeEntity> =
        animeDao.getAnimeByCategory(category)

    suspend fun insertAnime(anime: List<AnimeEntity>) =
        animeDao.insertAnime(anime)

    suspend fun deleteAnimeByCategory(category: String) =
        animeDao.deleteAnimeByCategory(category)

    suspend fun getCacheTime(category: String): Long? =
        animeDao.getCacheTime(category)

    // Saved list helpers
    suspend fun saveAnime(entity: AnimeEntity) =
        animeDao.insertAnime(listOf(entity))

    suspend fun deleteSavedById(category: String, id: Int) =
        animeDao.deleteSavedById(category, id)
}

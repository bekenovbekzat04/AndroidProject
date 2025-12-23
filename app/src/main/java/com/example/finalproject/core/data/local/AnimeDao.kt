package com.example.finalproject.core.data.local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AnimeDao {

    @Query("SELECT * FROM anime WHERE category = :category ORDER BY cachedAt DESC")
    suspend fun getAnimeByCategory(category: String): List<AnimeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: List<AnimeEntity>)

    @Query("DELETE FROM anime WHERE category = :category")
    suspend fun deleteAnimeByCategory(category: String)

    @Query("SELECT cachedAt FROM anime WHERE category = :category LIMIT 1")
    suspend fun getCacheTime(category: String): Long?

    @Query("DELETE FROM anime WHERE category = :category AND id = :id")
    suspend fun deleteSavedById(category: String, id: Int)
}

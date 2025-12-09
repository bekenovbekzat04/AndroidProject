package com.example.youtubeapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.youtubeapp.data.model.Video

@Dao
interface VideoDao {

    @Query("SELECT * FROM videos ORDER BY savedAt DESC")
    fun getAllVideos(): LiveData<List<Video>>

    @Query("SELECT * FROM videos WHERE isFavorite = 1 ORDER BY savedAt DESC")
    fun getFavoriteVideos(): LiveData<List<Video>>

    @Query("SELECT * FROM videos WHERE id = :videoId")
    suspend fun getVideoById(videoId: String): Video?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: Video)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<Video>)

    @Update
    suspend fun updateVideo(video: Video)

    @Delete
    suspend fun deleteVideo(video: Video)

    @Query("DELETE FROM videos")
    suspend fun clearAllVideos()

    @Query("UPDATE videos SET isFavorite = :isFavorite WHERE id = :videoId")
    suspend fun updateFavoriteStatus(videoId: String, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM videos")
    suspend fun getVideoCount(): Int
}
package com.example.finalproject.core.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finalproject.core.domain.model.Anime

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val category: String,
    val cachedAt: Long = System.currentTimeMillis()
)

fun AnimeEntity.toDomain() = Anime(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl
)

fun Anime.toEntity(category: String) = AnimeEntity(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    category = category
)
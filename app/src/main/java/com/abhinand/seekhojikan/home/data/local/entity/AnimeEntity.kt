package com.abhinand.seekhojikan.home.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abhinand.seekhojikan.home.data.remote.dto.NamedResourceDto

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey
    val malId: Int,
    val title: String,
    val imageUrl: String,
    val score: Double?,
    val year: Int?,
    val synopsis: String?,
    val rank: Int?,
    val episodes: Int?,
    val youtubeId: String?,
    val genres: List<NamedResourceDto>,
    val rating: String
)

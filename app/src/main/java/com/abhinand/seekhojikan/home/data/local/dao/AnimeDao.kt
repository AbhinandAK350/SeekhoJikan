package com.abhinand.seekhojikan.home.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.abhinand.seekhojikan.home.data.local.entity.AnimeEntity

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeList(animeList: List<AnimeEntity>)

    @Query("SELECT * FROM anime")
    suspend fun getAnimeList(): List<AnimeEntity>

    @Query("SELECT * FROM anime WHERE malId = :id")
    suspend fun getAnime(id: Int): AnimeEntity?

    @Update
    suspend fun updateAnime(anime: AnimeEntity)

}
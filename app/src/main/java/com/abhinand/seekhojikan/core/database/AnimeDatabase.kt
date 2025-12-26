package com.abhinand.seekhojikan.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abhinand.seekhojikan.home.data.local.dao.AnimeDao
import com.abhinand.seekhojikan.home.data.local.entity.AnimeEntity

@Database(
    entities = [AnimeEntity::class],
    version = 1
)
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
}
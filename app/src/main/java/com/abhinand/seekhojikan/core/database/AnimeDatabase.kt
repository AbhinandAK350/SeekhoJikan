package com.abhinand.seekhojikan.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abhinand.seekhojikan.home.data.local.converters.ListConverter
import com.abhinand.seekhojikan.home.data.local.dao.AnimeDao
import com.abhinand.seekhojikan.home.data.local.entity.AnimeEntity

@Database(
    entities = [AnimeEntity::class],
    version = 2
)
@TypeConverters(ListConverter::class)
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
}
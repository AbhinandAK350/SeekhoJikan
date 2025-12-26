package com.abhinand.seekhojikan.home.data.local.converters

import androidx.room.TypeConverter
import com.abhinand.seekhojikan.home.data.remote.dto.NamedResourceDto
import kotlinx.serialization.json.Json

class ListConverter {
    @TypeConverter
    fun fromList(list: List<NamedResourceDto>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toList(json: String): List<NamedResourceDto> {
        return Json.decodeFromString(json)
    }
}
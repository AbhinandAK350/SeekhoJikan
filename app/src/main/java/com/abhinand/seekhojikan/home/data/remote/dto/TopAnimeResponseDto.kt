package com.abhinand.seekhojikan.home.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TopAnimeResponseDto(
    val pagination: PaginationDto,
    val data: List<AnimeDto>
)

@Serializable
data class PaginationDto(
    val last_visible_page: Int,
    val has_next_page: Boolean,
    val current_page: Int,
    val items: PaginationItemsDto
)

@Serializable
data class PaginationItemsDto(
    val count: Int,
    val total: Int,
    val per_page: Int
)

@Serializable
data class AnimeDto(
    val mal_id: Int,
    val url: String,
    val images: AnimeImagesDto,
    val trailer: TrailerDto,
    val approved: Boolean,
    val titles: List<TitleDto>,
    val title: String,
    val title_english: String? = null,
    val title_japanese: String? = null,
    val title_synonyms: List<String>,
    val type: String,
    val source: String,
    val episodes: Int? = null,
    val status: String,
    val airing: Boolean,
    val aired: AiredDto,
    val duration: String,
    val rating: String,
    val score: Double? = null,
    val scored_by: Int? = null,
    val rank: Int? = null,
    val popularity: Int,
    val members: Int,
    val favorites: Int,
    val synopsis: String? = null,
    val background: String? = null,
    val season: String? = null,
    val year: Int? = null,
    val broadcast: BroadcastDto? = null,
    val producers: List<NamedResourceDto>,
    val licensors: List<NamedResourceDto>,
    val studios: List<NamedResourceDto>,
    val genres: List<NamedResourceDto>,
    val explicit_genres: List<NamedResourceDto>,
    val themes: List<NamedResourceDto>,
    val demographics: List<NamedResourceDto>
)

@Serializable
data class AnimeImagesDto(
    val jpg: ImageSetDto,
    val webp: ImageSetDto
)

@Serializable
data class ImageSetDto(
    val image_url: String,
    val small_image_url: String,
    val large_image_url: String
)

@Serializable
data class TrailerDto(
    val youtube_id: String? = null,
    val url: String? = null,
    val embed_url: String? = null,
    val images: TrailerImagesDto
)

@Serializable
data class TrailerImagesDto(
    val image_url: String? = null,
    val small_image_url: String? = null,
    val medium_image_url: String? = null,
    val large_image_url: String? = null,
    val maximum_image_url: String? = null
)

@Serializable
data class TitleDto(
    val type: String,
    val title: String
)

@Serializable
data class AiredDto(
    val from: String? = null,
    val to: String? = null,
    val prop: AiredPropDto,
    val string: String
)

@Serializable
data class AiredPropDto(
    val from: DatePropDto,
    val to: DatePropDto? = null
)

@Serializable
data class DatePropDto(
    val day: Int? = null,
    val month: Int? = null,
    val year: Int? = null
)

@Serializable
data class BroadcastDto(
    val day: String? = null,
    val time: String? = null,
    val timezone: String? = null,
    val string: String? = null
)

@Serializable
data class NamedResourceDto(
    val mal_id: Int,
    val type: String,
    val name: String,
    val url: String
)

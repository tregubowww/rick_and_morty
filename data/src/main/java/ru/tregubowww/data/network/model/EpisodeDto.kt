package ru.tregubowww.data.network.model

import kotlinx.serialization.*

@Serializable
data class EpisodeDto(
    val info: EpisodeDtoInfo,
    val results: List<EpisodeDtoResult>,
)

@Serializable
data class EpisodeDtoInfo(
    val count: Long,
    val pages: Long,
    val next: String? = null,
    val prev: String? = null,
)

@Serializable
data class EpisodeDtoResult(
    val id: Long,
    val name: String,

    @SerialName("air_date")
    val airDate: String,

    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String,
)
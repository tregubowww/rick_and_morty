package ru.tregubowww.data.network.model

import kotlinx.serialization.*

@Serializable
data class LocationDto(
    val info: LocationDtoInfo,
    val results: List<LocationDtoResult>,
)

@Serializable
data class LocationDtoInfo(
    val count: Long,
    val pages: Long,
    val next: String? = null,
    val prev: String? = null,
)

@Serializable
data class LocationDtoResult(
    val id: Long,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String,
)
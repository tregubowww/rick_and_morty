package ru.tregubowww.data.network.model

import kotlinx.serialization.*

@Serializable
data class CharactersDto(
    val info: CharactersDtoInfo,
    val results: List<CharactersDtoResult>,
)

@Serializable
data class CharactersDtoInfo(
    val count: Long,
    val pages: Long,
    val next: String? = null,
    val prev: String? = null,
)

@Serializable
data class CharactersDtoResult(
    val id: Long,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: CharactersDtoResultLocation,
    val location: CharactersDtoResultLocation,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,

    )

@Serializable
data class CharactersDtoResultLocation(
    val name: String,
    val url: String,
)


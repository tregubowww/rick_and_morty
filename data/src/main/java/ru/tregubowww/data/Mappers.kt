package ru.tregubowww.data

import ru.tregubowww.data.network.model.CharactersDtoResult
import ru.tregubowww.data.network.model.EpisodeDtoResult
import ru.tregubowww.data.network.model.LocationDtoResult
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.domain.entity.Location

internal fun CharactersDtoResult.toCharacter(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        originIdLocation = origin.url.substringAfterLast("/").toLongOrNull(),
        originNameLocation = origin.name,
        nameLocation = location.name,
        idLocation = location.url.substringAfterLast("/").toLongOrNull(),
        type = type,
        episodesId = episode.filter { it.isNotBlank() }.joinToString { it.substringAfterLast("/") })
}

internal fun EpisodeDtoResult.toEpisode(): Episode =
    Episode(
        id = id,
        name = name,
        airDate = airDate,
        episode = episode,
        characters = characters.filter { it.isNotBlank() }.joinToString { it.substringAfterLast("/") })

internal fun LocationDtoResult.toLocation(): Location =
    Location(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        residents = residents.filter { it.isNotBlank() }.joinToString { it.substringAfterLast("/") })



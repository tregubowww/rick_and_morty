package ru.tregubowww.domain.interactor

import androidx.paging.PagingSource
import ru.tregubowww.domain.repository.CharacterRepository
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.domain.query.FilterQueryCharacter
import javax.inject.Inject

interface CharactersInteractor {
    fun getAllCharacters(): PagingSource<Int, Character>
    fun searchCharacter(querySearch: String): PagingSource<Int, Character>
    suspend fun searchCharacterFromDb(querySearch: String): List<Character>

    suspend fun getCharacterFromNetwork(characterId: Long): Character
    suspend fun getCharacterFromDb(characterId: Long): Character
    suspend fun getCharacterListFromDb(): List<Character>
    fun filterCharacters(queryFilterCharacter: FilterQueryCharacter): PagingSource<Int, Character>
    suspend fun filterCharactersFromDb(queryFilterCharacter: FilterQueryCharacter): List<Character>
    suspend fun getCharacterListById(charactersId: String): List<Character>
    suspend fun getCharacterListByIdFromDb(charactersId: String): List<Character>
}

class CharactersInteractorImpl @Inject constructor(
    private val characterRepository: CharacterRepository,
) : CharactersInteractor {

    override fun getAllCharacters(): PagingSource<Int, Character> =
        characterRepository.getCharacters()

    override fun searchCharacter(querySearch: String): PagingSource<Int, Character> =
        characterRepository.getSearchCharacter(querySearch)

    override suspend fun searchCharacterFromDb(querySearch: String) =
        characterRepository.searchCharacterFromDb(querySearch)

    override suspend fun getCharacterFromNetwork(characterId: Long): Character =
        characterRepository.getCharacterFromNetwork(characterId)

    override suspend fun getCharacterFromDb(characterId: Long): Character =
        characterRepository.getCharacterFromDb(characterId)

    override suspend fun getCharacterListFromDb(): List<Character> =
        characterRepository.getCharacterListFromDb()

    override fun filterCharacters(queryFilterCharacter: FilterQueryCharacter): PagingSource<Int, Character> =
        characterRepository.getFilterCharacters(queryFilterCharacter)

    override suspend fun filterCharactersFromDb(queryFilterCharacter: FilterQueryCharacter): List<Character> =
        characterRepository.getFilterCharactersFromDb(queryFilterCharacter)

    override suspend fun getCharacterListById(charactersId: String): List<Character> =
        characterRepository.getCharacterListById(charactersId)

    override suspend fun getCharacterListByIdFromDb(charactersId: String): List<Character> =
        characterRepository.getCharacterListByIdFromDb(charactersId)
}
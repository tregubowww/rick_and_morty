package ru.tregubowww.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.tregubowww.domain.entity.Character

@Dao
interface CharacterDao {

    @Query("SELECT*  FROM characters")
    suspend fun getListCharacter(): List<Character>

    @Insert
        (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCharacters(characters: List<Character>)

    @Query("SELECT * FROM characters WHERE name LIKE :searchQuery")
    suspend fun searchCharacter(searchQuery: String): List<Character>

    @Query("SELECT * FROM characters WHERE name LIKE :name " +
            "AND status LIKE :status AND species LIKE :species AND type LIKE :type AND gender LIKE :gender")
    suspend fun filterCharacter(name: String, status: String, species: String, type: String, gender: String): List<Character>

    @Query("SELECT * FROM characters WHERE id = :id ")
    suspend fun getCharacter(id: Long): Character
}
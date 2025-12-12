package com.mariogarluu.dragonballapi.Data.Local

import androidx.room.*
import com.mariogarluu.dragonballapi.Data.Local.entity.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the character table.
 */
@Dao
interface CharacterDao {
    /**
     * Inserts a list of characters into the database.
     *
     * @param characters The list of characters to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    /**
     * Inserts a single character into the database.
     *
     * @param character The character to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity)

    /**
     * Inserts a list of transformations into the database.
     *
     * @param transformations The list of transformations to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransformations(transformations: List<TransformationEntity>)

    /**
     * Gets a flow of all characters with their relations from the database.
     *
     * @return A flow of a list of [CharacterWithRelations].
     */
    @Transaction
    @Query("SELECT * FROM characters")
    fun getAllCharactersWithRelations(): Flow<List<CharacterWithRelations>>

    /**
     * Gets a flow of a single character with its relations by its ID from the database.
     *
     * @param id The ID of the character to get.
     * @return A flow of a [CharacterWithRelations] or null if not found.
     */
    @Transaction
    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacterWithRelationsById(id: Int): Flow<CharacterWithRelations?>

    /**
     * Deletes a character by its ID from the database.
     *
     * @param id The ID of the character to delete.
     */
    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun deleteCharacterById(id: Int)

    /**
     * Updates the favorite status of a character.
     *
     * @param id The ID of the character to update.
     * @param isFavorite The new favorite status.
     */
    @Query("UPDATE characters SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)
}
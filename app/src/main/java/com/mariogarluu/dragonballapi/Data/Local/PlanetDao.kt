package com.mariogarluu.dragonballapi.Data.Local

import androidx.room.*
import com.mariogarluu.dragonballapi.Data.Local.entity.PlanetEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the planet table.
 */
@Dao
interface PlanetDao {
    /**
     * Inserts a list of planets into the database.
     *
     * @param planets The list of planets to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(planets: List<PlanetEntity>)

    /**
     * Inserts a single planet into the database.
     *
     * @param planet The planet to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanet(planet: PlanetEntity)

    /**
     * Gets a flow of all planets from the database.
     *
     * @return A flow of a list of [PlanetEntity].
     */
    @Query("SELECT * FROM planets")
    fun getAllPlanets(): Flow<List<PlanetEntity>>

    /**
     * Gets a flow of a single planet by its ID from the database.
     *
     * @param id The ID of the planet to get.
     * @return A flow of a [PlanetEntity] or null if not found.
     */
    @Query("SELECT * FROM planets WHERE id = :id")
    fun getPlanetById(id: Int): Flow<PlanetEntity?>

    /**
     * Deletes a planet by its ID from the database.
     *
     * @param id The ID of the planet to delete.
     */
    @Query("DELETE FROM planets WHERE id = :id")
    suspend fun deletePlanetById(id: Int)

    /**
     * Updates the favorite status of a planet.
     *
     * @param id The ID of the planet to update.
     * @param isFavorite The new favorite status.
     */
    @Query("UPDATE planets SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)
}
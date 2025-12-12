package com.mariogarluu.dragonballapi.Data.Repo

import androidx.room.withTransaction
import com.mariogarluu.dragonballapi.Data.Local.CharacterDao
import com.mariogarluu.dragonballapi.Data.Local.DragonBallDatabase
import com.mariogarluu.dragonballapi.Data.Local.PlanetDao
import com.mariogarluu.dragonballapi.Data.Model.Character
import com.mariogarluu.dragonballapi.Data.Model.Planet
import com.mariogarluu.dragonballapi.Data.Remote.DragonBallApi
import com.mariogarluu.dragonballapi.Data.toDomain
import com.mariogarluu.dragonballapi.Data.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository for fetching Dragon Ball data from the network and storing it in the local database.
 *
 * @property api The remote API service.
 * @property db The local database.
 * @property characterDao The DAO for characters.
 * @property planetDao The DAO for planets.
 */
class DragonBallRepository @Inject constructor(
    private val api: DragonBallApi,
    private val db: DragonBallDatabase,
    private val characterDao: CharacterDao,
    private val planetDao: PlanetDao
) {
    // Personajes

    /**
     * Gets a flow of all characters from the local database.
     * The data is mapped to domain models.
     */
    fun getCharacters() = characterDao.getAllCharactersWithRelations().map { list -> list.map { it.toDomain() } }.flowOn(Dispatchers.IO)

    /**
     * Refreshes the characters from the remote API and stores them in the local database.
     */
    suspend fun refreshCharacters() = withContext(Dispatchers.IO) {
        runCatching {
            val res = api.getCharacters(100)
            val chars = res.items
            val planets = chars.mapNotNull { it.originPlanet }
            val trans = chars.flatMap { c -> c.transformations?.map { it.toEntity(c.id) }.orEmpty() }
            db.withTransaction {
                if (planets.isNotEmpty()) planetDao.insertAll(planets.map { it.toEntity() })
                characterDao.insertCharacters(chars.map { it.toEntity() })
                if (trans.isNotEmpty()) characterDao.insertTransformations(trans)
            }
        }
    }

    /**
     * Gets a flow of a single character by its ID from the local database.
     * The data is mapped to a domain model.
     *
     * @param id The ID of the character to get.
     */
    fun getCharacterById(id: Int) = characterDao.getCharacterWithRelationsById(id).map { it?.toDomain() }.flowOn(Dispatchers.IO)

    /**
     * Refreshes a single character by its ID from the remote API and stores it in the local database.
     *
     * @param id The ID of the character to refresh.
     */
    suspend fun refreshCharacterById(id: Int) = withContext(Dispatchers.IO) {
        runCatching {
            val c = api.getCharacterById(id)
            db.withTransaction {
                c.originPlanet?.let { planetDao.insertAll(listOf(it.toEntity())) }
                characterDao.insertCharacters(listOf(c.toEntity()))
                val t = c.transformations?.map { it.toEntity(c.id) }.orEmpty()
                if (t.isNotEmpty()) characterDao.insertTransformations(t)
            }
        }
    }

    /**
     * Toggles the favorite status of a character.
     *
     * @param id The ID of the character.
     * @param isFav The new favorite status.
     */
    suspend fun toggleFavorite(id: Int, isFav: Boolean) = withContext(Dispatchers.IO) { characterDao.updateFavoriteStatus(id, isFav) }

    /**
     * Deletes a character by its ID from the local database.
     *
     * @param id The ID of the character to delete.
     */
    suspend fun deleteCharacter(id: Int) = withContext(Dispatchers.IO) { characterDao.deleteCharacterById(id) }

    /**
     * Adds a local character to the database.
     *
     * @param c The character to add.
     */
    suspend fun addLocalCharacter(c: Character) = withContext(Dispatchers.IO) { characterDao.insertCharacter(c.toEntity()) }

    // Planetas

    /**
     * Gets a flow of all planets from the local database.
     * The data is mapped to domain models.
     */
    fun getPlanets() = planetDao.getAllPlanets().map { list -> list.map { it.toDomain() } }.flowOn(Dispatchers.IO)

    /**
     * Refreshes the planets from the remote API and stores them in the local database.
     */
    suspend fun refreshPlanets() = withContext(Dispatchers.IO) {
        runCatching {
            val res = api.getPlanets(100)
            planetDao.insertAll(res.items.map { it.toEntity() })
        }
    }

    /**
     * Gets a flow of a single planet by its ID from the local database.
     * The data is mapped to a domain model.
     *
     * @param id The ID of the planet to get.
     */
    fun getPlanetById(id: Int) = planetDao.getPlanetById(id).map { it?.toDomain() }.flowOn(Dispatchers.IO)

    /**
     * Refreshes a single planet by its ID from the remote API and stores it in the local database.
     *
     * @param id The ID of the planet to refresh.
     */
    suspend fun refreshPlanetById(id: Int) = withContext(Dispatchers.IO) {
        runCatching {
            val p = api.getPlanetById(id)
            planetDao.insertAll(listOf(p.toEntity()))
        }
    }

    /**
     * Toggles the favorite status of a planet.
     *
     * @param id The ID of the planet.
     * @param isFav The new favorite status.
     */
    suspend fun togglePlanetFavorite(id: Int, isFav: Boolean) = withContext(Dispatchers.IO) { planetDao.updateFavoriteStatus(id, isFav) }

    /**
     * Deletes a planet by its ID from the local database.
     *
     * @param id The ID of the planet to delete.
     */
    suspend fun deletePlanet(id: Int) = withContext(Dispatchers.IO) { planetDao.deletePlanetById(id) }

    /**
     * Adds a local planet to the database.
     *
     * @param p The planet to add.
     */
    suspend fun addLocalPlanet(p: Planet) = withContext(Dispatchers.IO) { planetDao.insertPlanet(p.toEntity()) }
}
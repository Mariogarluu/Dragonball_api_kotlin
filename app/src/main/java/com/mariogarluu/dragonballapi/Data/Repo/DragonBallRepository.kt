package com.mariogarluu.dragonballapi.data.repo

import androidx.room.withTransaction
import com.mariogarluu.dragonballapi.data.local.CharacterDao
import com.mariogarluu.dragonballapi.data.local.DragonBallDatabase
import com.mariogarluu.dragonballapi.data.local.PlanetDao
import com.mariogarluu.dragonballapi.data.remote.DragonBallApi
import com.mariogarluu.dragonballapi.data.toDomain
import com.mariogarluu.dragonballapi.data.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DragonBallRepository @Inject constructor(
    private val api: DragonBallApi,
    private val db: DragonBallDatabase,
    private val characterDao: CharacterDao,
    private val planetDao: PlanetDao
) {

    // --- Personajes ---

    // Observa la base de datos (SSOT). Cualquier cambio se emite automáticamente.
    fun getCharacters() = characterDao.getAllCharactersWithRelations()
        .map { list ->
            list.map { it.toDomain() }
        }
        .flowOn(Dispatchers.IO)

    // Actualiza desde la red y guarda en BD. No devuelve datos, solo actualiza la caché.
    suspend fun refreshCharacters(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val response = api.getCharacters(100)
            val characters = response.items

            // Extraer planetas y transformaciones para mantener integridad referencial
            val planets = characters.mapNotNull { it.originPlanet }

            // Mapeo seguro de transformaciones
            val transformations = characters.flatMap { char ->
                char.transformations?.map { trans -> trans.toEntity(char.id) } ?: emptyList()
            }

            db.withTransaction {
                // El orden importa por las Foreign Keys
                if (planets.isNotEmpty()) {
                    planetDao.insertAll(planets.map { it.toEntity() })
                }
                characterDao.insertCharacters(characters.map { it.toEntity() })
                if (transformations.isNotEmpty()) {
                    characterDao.insertTransformations(transformations)
                }
            }
        }
    }

    fun getCharacterById(id: Int) = characterDao.getCharacterWithRelationsById(id)
        .map { it?.toDomain() }
        .flowOn(Dispatchers.IO)

    suspend fun refreshCharacterById(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val character = api.getCharacterById(id)
            db.withTransaction {
                character.originPlanet?.let {
                    planetDao.insertAll(listOf(it.toEntity()))
                }
                characterDao.insertCharacters(listOf(character.toEntity()))

                val transformations = character.transformations?.map {
                    it.toEntity(character.id)
                } ?: emptyList()

                if (transformations.isNotEmpty()) {
                    characterDao.insertTransformations(transformations)
                }
            }
        }
    }

    // --- Operaciones Locales ---

    suspend fun toggleFavorite(id: Int, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        characterDao.updateFavoriteStatus(id, isFavorite)
    }

    suspend fun deleteCharacter(id: Int) = withContext(Dispatchers.IO) {
        characterDao.deleteCharacterById(id)
    }

    suspend fun addLocalCharacter(c: com.mariogarluu.dragonballapi.data.model.Character) = withContext(Dispatchers.IO) {
        characterDao.insertCharacter(c.toEntity())
    }

    // --- Planetas ---

    fun getPlanets() = planetDao.getAllPlanets()
        .map { list ->
            list.map { it.toDomain() }
        }
        .flowOn(Dispatchers.IO)

    suspend fun refreshPlanets(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val response = api.getPlanets(100)
            planetDao.insertAll(response.items.map { it.toEntity() })
        }
    }

    fun getPlanetById(id: Int) = planetDao.getPlanetById(id)
        .map { it?.toDomain() }
        .flowOn(Dispatchers.IO)

    suspend fun refreshPlanetById(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val planet = api.getPlanetById(id)
            planetDao.insertAll(listOf(planet.toEntity()))
        }
    }

    suspend fun togglePlanetFavorite(id: Int, isFav: Boolean) = withContext(Dispatchers.IO) {
        planetDao.updateFavoriteStatus(id, isFav)
    }

    suspend fun deletePlanet(id: Int) = withContext(Dispatchers.IO) {
        planetDao.deletePlanetById(id)
    }

    suspend fun addLocalPlanet(p: com.mariogarluu.dragonballapi.data.model.Planet) = withContext(Dispatchers.IO) {
        planetDao.insertPlanet(p.toEntity())
    }
}
package com.mariogarluu.dragonballapi.data.remote

import com.mariogarluu.dragonballapi.data.model.Character
import com.mariogarluu.dragonballapi.data.model.CharacterResponse
import com.mariogarluu.dragonballapi.data.model.Planet
import com.mariogarluu.dragonballapi.data.model.PlanetResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines the remote API for fetching Dragon Ball data.
 */
interface DragonBallApi {
    /**
     * Gets a list of characters from the API.
     *
     * @param limit The maximum number of characters to return.
     * @return A [CharacterResponse] containing the list of characters.
     */
    @GET("characters")
    suspend fun getCharacters(@Query("limit") limit: Int): CharacterResponse

    /**
     * Gets a single character by its ID from the API.
     *
     * @param id The ID of the character to get.
     * @return The [Character] object.
     */
    @GET("characters/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): Character

    /**
     * Gets a list of planets from the API.
     *
     * @param limit The maximum number of planets to return.
     * @return A [PlanetResponse] containing the list of planets.
     */
    @GET("planets")
    suspend fun getPlanets(@Query("limit") limit: Int): PlanetResponse

    /**
     * Gets a single planet by its ID from the API.
     *
     * @param id The ID of the planet to get.
     * @return The [Planet] object.
     */
    @GET("planets/{id}")
    suspend fun getPlanetById(@Path("id") id: Int): Planet
}
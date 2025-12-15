package com.mariogarluu.dragonballapi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents the response from the API when fetching a list of characters.
 *
 * @property items The list of characters.
 * @property meta Metadata about the response.
 */
data class CharacterResponse(val items: List<Character>, val meta: Meta)

/**
 * Represents the response from the API when fetching a list of planets.
 *
 * @property items The list of planets.
 * @property meta Metadata about the response.
 */
data class PlanetResponse(val items: List<Planet>, val meta: Meta)

/**
 * Represents the metadata of a response from the API.
 *
 * @property totalItems The total number of items available.
 * @property itemCount The number of items in the current response.
 * @property itemsPerPage The number of items per page.
 * @property totalPages The total number of pages.
 * @property currentPage The current page number.
 */
data class Meta(val totalItems: Int, val itemCount: Int, val itemsPerPage: Int, val totalPages: Int, val currentPage: Int)

/**
 * Represents a character.
 *
 * @property id The unique ID of the character.
 * @property name The name of the character.
 * @property ki The ki of the character.
 * @property maxKi The maximum ki of the character.
 * @property race The race of the character.
 * @property gender The gender of the character.
 * @property description A description of the character.
 * @property image The URL of the character's image.
 * @property affiliation The affiliation of the character.
 * @property originPlanet The character's planet of origin.
 * @property transformations A list of the character's transformations.
 * @property isFavorite Whether the character is a favorite.
 */
@Entity(tableName = "characters")
data class Character(
    @PrimaryKey val id: Int,
    val name: String, val ki: String, val maxKi: String, val race: String,
    val gender: String, val description: String, val image: String, val affiliation: String,
    val originPlanet: Planet? = null,
    val transformations: List<Transformation>? = emptyList(),
    val isFavorite: Boolean = false
)

/**
 * Represents a transformation of a character.
 *
 * @property id The unique ID of the transformation.
 * @property name The name of the transformation.
 * @property image The URL of the transformation's image.
 * @property ki The ki of the transformation.
 */
data class Transformation(val id: Int, val name: String, val image: String, val ki: String)

/**
 * Represents a planet.
 *
 * @property id The unique ID of the planet.
 * @property name The name of the planet.
 * @property isDestroyed Whether the planet has been destroyed.
 * @property description A description of the planet.
 * @property image The URL of the planet's image.
 * @property isFavorite Whether the planet is a favorite.
 */
@Entity(tableName = "planets")
data class Planet(
    @PrimaryKey val id: Int,
    val name: String, val isDestroyed: Boolean, val description: String, val image: String,
    val isFavorite: Boolean = false
)
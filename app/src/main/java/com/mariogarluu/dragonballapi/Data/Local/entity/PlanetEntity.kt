package com.mariogarluu.dragonballapi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a planet in the database.
 *
 * @property id The unique ID of the planet.
 * @property name The name of the planet.
 * @property isDestroyed Whether the planet has been destroyed.
 * @property description A description of the planet.
 * @property image The URL of the planet's image.
 * @property isFavorite Whether the planet is a favorite.
 */
@Entity(tableName = "planets")
data class PlanetEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val isDestroyed: Boolean,
    val description: String,
    val image: String,
    val isFavorite: Boolean = false
)
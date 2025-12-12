package com.mariogarluu.dragonballapi.Data.Local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a character in the database.
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
 * @property originPlanetId The ID of the character's planet of origin.
 * @property isFavorite Whether the character is a favorite.
 */
@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val ki: String,
    val maxKi: String,
    val race: String,
    val gender: String,
    val description: String,
    val image: String,
    val affiliation: String,
    val originPlanetId: Int?,
    val isFavorite: Boolean = false
)
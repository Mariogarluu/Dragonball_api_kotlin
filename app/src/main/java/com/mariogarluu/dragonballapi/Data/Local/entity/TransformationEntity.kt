package com.mariogarluu.dragonballapi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a transformation of a character in the database.
 *
 * @property id The unique ID of the transformation.
 * @property name The name of the transformation.
 * @property image The URL of the transformation's image.
 * @property ki The ki of the transformation.
 * @property characterId The ID of the character this transformation belongs to.
 */
@Entity(tableName = "transformations")
data class TransformationEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val image: String,
    val ki: String,
    val characterId: Int
)
package com.mariogarluu.dragonballapi.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Represents a character with its relations (planet and transformations).
 * This is used to query the database for a character and all its related data.
 *
 * @property character The character entity.
 * @property planet The character's planet of origin.
 * @property transformations A list of the character's transformations.
 */
data class CharacterWithRelations(
    @Embedded val character: CharacterEntity,
    @Relation(parentColumn = "originPlanetId", entityColumn = "id")
    val planet: PlanetEntity?,
    @Relation(parentColumn = "id", entityColumn = "characterId")
    val transformations: List<TransformationEntity>
)
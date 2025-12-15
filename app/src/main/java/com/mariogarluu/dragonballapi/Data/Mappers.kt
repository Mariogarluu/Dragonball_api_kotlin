package com.mariogarluu.dragonballapi.data

import com.mariogarluu.dragonballapi.data.local.entity.*
import com.mariogarluu.dragonballapi.data.model.*

fun CharacterWithRelations.toDomain(): Character = Character(
    id = character.id, name = character.name, ki = character.ki, maxKi = character.maxKi,
    race = character.race, gender = character.gender, description = character.description,
    image = character.image, affiliation = character.affiliation,
    originPlanet = planet?.toDomain(),
    transformations = transformations.map { it.toDomain() },
    isFavorite = character.isFavorite
)

fun PlanetEntity.toDomain(): Planet = Planet(
    id = id, name = name, isDestroyed = isDestroyed, description = description, image = image, isFavorite = isFavorite
)

fun TransformationEntity.toDomain(): Transformation = Transformation(
    id = id, name = name, image = image, ki = ki
)

fun Character.toEntity(): CharacterEntity = CharacterEntity(
    id = id, name = name, ki = ki, maxKi = maxKi, race = race, gender = gender,
    description = description, image = image, affiliation = affiliation,
    originPlanetId = originPlanet?.id, isFavorite = isFavorite
)

fun Planet.toEntity(): PlanetEntity = PlanetEntity(
    id = id, name = name, isDestroyed = isDestroyed, description = description, image = image, isFavorite = isFavorite
)

fun Transformation.toEntity(characterId: Int): TransformationEntity = TransformationEntity(
    id = id, name = name, image = image, ki = ki, characterId = characterId
)
package com.mariogarluu.dragonballapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mariogarluu.dragonballapi.data.local.entity.CharacterEntity
import com.mariogarluu.dragonballapi.data.local.entity.PlanetEntity
import com.mariogarluu.dragonballapi.data.local.entity.TransformationEntity

/**
 * The Room database for this app.
 */
@Database(
    entities = [CharacterEntity::class, PlanetEntity::class, TransformationEntity::class],
    version = 5,
    exportSchema = false
)
abstract class DragonBallDatabase : RoomDatabase() {
    /**
     * Returns the Data Access Object for the character table.
     *
     * @return The DAO for the character table.
     */
    abstract fun characterDao(): CharacterDao

    /**
     * Returns the Data Access Object for the planet table.
     *
     * @return The DAO for the planet table.
     */
    abstract fun planetDao(): PlanetDao
}
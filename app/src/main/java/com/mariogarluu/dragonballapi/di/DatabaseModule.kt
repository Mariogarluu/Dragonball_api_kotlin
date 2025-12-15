package com.mariogarluu.dragonballapi.di

import android.content.Context
import androidx.room.Room
import com.mariogarluu.dragonballapi.data.local.CharacterDao
import com.mariogarluu.dragonballapi.data.local.DragonBallDatabase
import com.mariogarluu.dragonballapi.data.local.PlanetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DragonBallDatabase {
        return Room.databaseBuilder(
            context,
            DragonBallDatabase::class.java,
            "dragonball_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCharacterDao(database: DragonBallDatabase): CharacterDao {
        return database.characterDao()
    }

    @Provides
    fun providePlanetDao(database: DragonBallDatabase): PlanetDao {
        return database.planetDao()
    }
}
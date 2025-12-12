package com.mariogarluu.dragonballapi.ui

import com.mariogarluu.dragonballapi.Data.Model.Character
import com.mariogarluu.dragonballapi.Data.Model.Planet

// --- LISTAS ---

/**
 * Represents the state of the characters list screen.
 */
sealed class CharactersUiState {
    /**
     * The screen is in a loading state.
     */
    object Loading : CharactersUiState()

    /**
     * The characters were loaded successfully.
     *
     * @property characters The list of characters.
     */
    data class Success(val characters: List<Character>) : CharactersUiState()

    /**
     * An error occurred while loading the characters.
     *
     * @property message The error message.
     */
    data class Error(val message: String) : CharactersUiState()
}

/**
 * Represents the state of the planets list screen.
 */
sealed class PlanetsUiState {
    /**
     * The screen is in a loading state.
     */
    object Loading : PlanetsUiState()

    /**
     * The planets were loaded successfully.
     *
     * @property planets The list of planets.
     */
    data class Success(val planets: List<Planet>) : PlanetsUiState()

    /**
     * An error occurred while loading the planets.
     *
     * @property message The error message.
     */
    data class Error(val message: String) : PlanetsUiState()
}

// --- DETALLES ---

/**
 * Represents the state of the character detail screen.
 */
sealed class CharacterDetailUiState {
    /**
     * The screen is in a loading state.
     */
    object Loading : CharacterDetailUiState()

    /**
     * The character was loaded successfully.
     *
     * @property character The character.
     */
    data class Success(val character: Character) : CharacterDetailUiState()

    /**
     * An error occurred while loading the character.
     *
     * @property message The error message.
     */
    data class Error(val message: String) : CharacterDetailUiState()
}

/**
 * Represents the state of the planet detail screen.
 */
sealed class PlanetDetailUiState {
    /**
     * The screen is in a loading state.
     */
    object Loading : PlanetDetailUiState()

    /**
     * The planet was loaded successfully.
     *
     * @property planet The planet.
     */
    data class Success(val planet: Planet) : PlanetDetailUiState()

    /**
     * An error occurred while loading the planet.
     *
     * @property message The error message.
     */
    data class Error(val message: String) : PlanetDetailUiState()
}
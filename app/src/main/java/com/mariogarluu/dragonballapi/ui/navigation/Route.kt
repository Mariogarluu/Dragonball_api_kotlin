package com.mariogarluu.dragonballapi.ui.navigation

/**
 * Represents a route in the application's navigation graph.
 *
 * @property route The route string.
 */
sealed class Route(val route: String) {
    /**
     * The home screen route.
     */
    object Home : Route("home")

    /**
     * The character list screen route.
     */
    object CharacterList : Route("character_list")

    /**
     * The character detail screen route.
     * This is a dynamic route that accepts a character ID.
     */
    object CharacterDetail : Route("character_detail/{characterId}") {
        /**
         * Creates the route for a specific character.
         *
         * @param characterId The ID of the character.
         * @return The route string for the character detail screen.
         */
        fun createRoute(characterId: Int) = "character_detail/$characterId"
    }

    /**
     * The planet list screen route.
     */
    object PlanetList : Route("planet_list")

    /**
     * The planet detail screen route.
     * This is a dynamic route that accepts a planet ID.
     */
    object PlanetDetail : Route("planet_detail/{planetId}") {
        /**
         * Creates the route for a specific planet.
         *
         * @param planetId The ID of the planet.
         * @return The route string for the planet detail screen.
         */
        fun createRoute(planetId: Int) = "planet_detail/$planetId"
    }
}
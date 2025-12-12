package com.mariogarluu.dragonballapi.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mariogarluu.dragonballapi.ui.detail.DragonBallDetailScreen
import com.mariogarluu.dragonballapi.ui.home.HomeScreen
import com.mariogarluu.dragonballapi.ui.list.DragonBallListScreen
import com.mariogarluu.dragonballapi.ui.planets.detail.PlanetDetailScreen
import com.mariogarluu.dragonballapi.ui.planets.list.PlanetListScreen

/**
 * Defines the navigation graph for the application.
 *
 * @param navController The navigation controller.
 * @param contentPadding The padding for the content.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    contentPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home.route,
        modifier = Modifier.padding(contentPadding)
    ) {
        // HOME
        composable(Route.Home.route) {
            HomeScreen(
                onCharactersClick = { navController.navigate(Route.CharacterList.route) },
                onPlanetsClick = { navController.navigate(Route.PlanetList.route) }
            )
        }

        // LISTA PERSONAJES
        composable(Route.CharacterList.route) {
            DragonBallListScreen(
                onCharacterClick = { id -> 
                    navController.navigate(Route.CharacterDetail.createRoute(id)) 
                },
                onUpClick = { navController.popBackStack() }
            )
        }

        // DETALLE PERSONAJE
        composable(
            route = Route.CharacterDetail.route,
            arguments = listOf(navArgument("characterId") { type = NavType.IntType })
        ) {
            DragonBallDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // LISTA PLANETAS
        composable(Route.PlanetList.route) {
            PlanetListScreen(
                onPlanetClick = { id -> 
                    navController.navigate(Route.PlanetDetail.createRoute(id)) 
                },
                onUpClick = { navController.popBackStack() }
            )
        }

        // DETALLE PLANETA
        composable(
            route = Route.PlanetDetail.route,
            arguments = listOf(navArgument("planetId") { type = NavType.IntType })
        ) {
            PlanetDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
package com.mariogarluu.dragonballapi.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable that displays the home screen of the application.
 * It contains buttons to navigate to the character list and the planet list.
 *
 * @param onCharactersClick The callback to be invoked when the characters button is clicked.
 * @param onPlanetsClick The callback to be invoked when the planets button is clicked.
 */
@Composable
fun HomeScreen(
    onCharactersClick: () -> Unit,
    onPlanetsClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onCharactersClick, modifier = Modifier.padding(16.dp)) {
            Text(text = "Ver Personajes")
        }
        Button(onClick = onPlanetsClick, modifier = Modifier.padding(16.dp)) {
            Text(text = "Ver Planetas")
        }
    }
}
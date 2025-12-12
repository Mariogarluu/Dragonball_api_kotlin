package com.mariogarluu.dragonballapi.ui.planets.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mariogarluu.dragonballapi.ui.PlanetDetailUiState
import com.mariogarluu.dragonballapi.ui.common.AppTopBar

/**
 * Composable that displays the detail screen for a planet.
 *
 * @param onBack The callback to be invoked when the back button is clicked.
 * @param viewModel The view model for this screen.
 */
@Composable
fun PlanetDetailScreen(
    onBack: () -> Unit,
    viewModel: PlanetDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Detalle Planeta",
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onBack
            )
        },
        floatingActionButton = {
            if (uiState is PlanetDetailUiState.Success) {
                val planet = (uiState as PlanetDetailUiState.Success).planet
                FloatingActionButton(onClick = { viewModel.toggleFavorite(planet) }) {
                    Icon(
                        imageVector = if (planet.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (planet.isFavorite) Color.Red else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is PlanetDetailUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is PlanetDetailUiState.Error -> Text(text = state.message, modifier = Modifier.align(Alignment.Center))
                is PlanetDetailUiState.Success -> {
                    val planet = state.planet
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current).data(planet.image).crossfade(true).build(),
                            contentDescription = planet.name,
                            modifier = Modifier.fillMaxWidth().height(300.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = planet.name, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                            val statusText = if (planet.isDestroyed) "Destruido" else "Activo"
                            val statusColor = if (planet.isDestroyed) Color.Red else Color.Green
                            Text(text = "Estado: $statusText", style = MaterialTheme.typography.titleMedium, color = statusColor)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = planet.description, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}
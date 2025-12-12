package com.mariogarluu.dragonballapi.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mariogarluu.dragonballapi.ui.CharacterDetailUiState
import com.mariogarluu.dragonballapi.ui.common.AppTopBar

/**
 * Composable that displays the detail screen for a character.
 *
 * @param onBack The callback to be invoked when the back button is clicked.
 * @param viewModel The view model for this screen.
 */
@Composable
fun DragonBallDetailScreen(
    onBack: () -> Unit,
    viewModel: DragonBallDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Detalle del Personaje",
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onBack
            )
        },
        floatingActionButton = {
            if (uiState is CharacterDetailUiState.Success) {
                val character = (uiState as CharacterDetailUiState.Success).character
                FloatingActionButton(onClick = { viewModel.toggleFavorite(character) }) {
                    Icon(
                        imageVector = if (character.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (character.isFavorite) Color.Red else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is CharacterDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is CharacterDetailUiState.Error -> {
                    Text(text = state.message, modifier = Modifier.align(Alignment.Center))
                }
                is CharacterDetailUiState.Success -> {
                    val char = state.character
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current).data(char.image).crossfade(true).build(),
                            contentDescription = char.name,
                            modifier = Modifier.fillMaxWidth().height(300.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = char.name, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                        Text(text = char.race + " - " + char.gender, style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = char.description, style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            InfoChip(label = "Ki", value = char.ki)
                            InfoChip(label = "Max Ki", value = char.maxKi)
                        }
                        if (char.originPlanet != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Planeta de Origen", style = MaterialTheme.typography.titleLarge)
                            Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                                    AsyncImage(model = char.originPlanet.image, contentDescription = null, modifier = Modifier.size(60.dp).clip(CircleShape), contentScale = ContentScale.Crop)
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(text = char.originPlanet.name, fontWeight = FontWeight.Bold)
                                        Text(text = if(char.originPlanet.isDestroyed) "Destruido" else "Activo", color = if(char.originPlanet.isDestroyed) Color.Red else Color.Green)
                                    }
                                }
                            }
                        }
                        if (!char.transformations.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Transformaciones", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(char.transformations) { trans ->
                                    Card(modifier = Modifier.width(160.dp)) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            AsyncImage(model = trans.image, contentDescription = trans.name, modifier = Modifier.height(180.dp).fillMaxWidth(), contentScale = ContentScale.Crop)
                                            Text(text = trans.name, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold)
                                            Text(text = "Ki: ${trans.ki}", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(bottom = 8.dp))
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

/**
 * A composable that displays a label and a value.
 * This is used to display information about the character.
 *
 * @param label The label to display.
 * @param value The value to display.
 */
@Composable
fun InfoChip(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}
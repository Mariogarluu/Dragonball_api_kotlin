package com.mariogarluu.dragonballapi.ui.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mariogarluu.dragonballapi.ui.CharacterDetailUiState
import com.mariogarluu.dragonballapi.ui.common.AppTopBar

@Composable
fun DragonBallDetailScreen(
    onBack: () -> Unit,
    onPlanetClick: (Int) -> Unit,
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
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
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
                            Card(
                                onClick = { onPlanetClick(char.originPlanet.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                                    AsyncImage(
                                        model = char.originPlanet.image,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(text = char.originPlanet.name, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = if(char.originPlanet.isDestroyed) "Destruido" else "Activo",
                                            color = if(char.originPlanet.isDestroyed) Color.Red else Color.Green
                                        )
                                    }
                                }
                            }
                        }

                        if (!char.transformations.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Transformaciones", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(8.dp))

                            var currentTransformationIndex by remember { mutableIntStateOf(0) }
                            val transformations = char.transformations

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(
                                    onClick = { if (currentTransformationIndex > 0) currentTransformationIndex-- },
                                    enabled = currentTransformationIndex > 0
                                ) {
                                    if (currentTransformationIndex > 0) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Anterior",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 8.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    AnimatedContent(
                                        targetState = transformations[currentTransformationIndex],
                                        transitionSpec = {
                                            fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                                        },
                                        label = "TransformationAnimation"
                                    ) { trans ->
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            AsyncImage(
                                                model = trans.image,
                                                contentDescription = trans.name,
                                                modifier = Modifier
                                                    .height(450.dp)
                                                    .fillMaxWidth(),
                                                contentScale = ContentScale.Fit
                                            )

                                            Column(
                                                modifier = Modifier
                                                    .padding(12.dp)
                                                    .fillMaxWidth(),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "Ki: ${trans.ki}",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                Spacer(modifier = Modifier.height(4.dp))

                                                Text(
                                                    text = trans.name,
                                                    style = MaterialTheme.typography.headlineSmall,
                                                    fontWeight = FontWeight.Black,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }

                                IconButton(
                                    onClick = { if (currentTransformationIndex < transformations.size - 1) currentTransformationIndex++ },
                                    enabled = currentTransformationIndex < transformations.size - 1
                                ) {
                                    if (currentTransformationIndex < transformations.size - 1) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = "Siguiente",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
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

@Composable
fun InfoChip(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}
package com.mariogarluu.dragonballapi.ui.planets.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mariogarluu.dragonballapi.data.model.Planet
import com.mariogarluu.dragonballapi.ui.PlanetsUiState
import com.mariogarluu.dragonballapi.ui.common.AppTopBar

/**
 * Composable that displays the list of planets.
 *
 * @param onPlanetClick Called when a planet is clicked.
 * @param onUpClick Called when the up button is clicked.
 * @param viewModel The view model for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanetListScreen(
    onPlanetClick: (Int) -> Unit,
    onUpClick: () -> Unit,
    viewModel: PlanetListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isFilterActive by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                AppTopBar(
                    title = "Planetas",
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    onClick = onUpClick
                )
                PlanetSearchBar(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                        viewModel.onSearchQueryChanged(it)
                    },
                    isFilterActive = isFilterActive,
                    onFilterClick = {
                        isFilterActive = !isFilterActive
                        viewModel.toggleFavoriteFilter()
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Planeta")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is PlanetsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is PlanetsUiState.Success -> {
                    if (state.planets.isEmpty()) {
                        Text("No hay planetas", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(items = state.planets, key = { it.id }) { planet ->
                                PlanetSwipeToDeleteContainer(
                                    onDelete = { viewModel.deletePlanet(planet.id) }
                                ) {
                                    PlanetItem(
                                        planet = planet,
                                        onPlanetClick = { onPlanetClick(planet.id) },
                                        onFavoriteClick = { viewModel.togglePlanetFavorite(planet) }
                                    )
                                }
                            }
                        }
                    }
                }
                is PlanetsUiState.Error -> {
                    Text(text = state.message, modifier = Modifier.align(Alignment.Center))
                }
            }

            if (showAddDialog) {
                AddPlanetDialog(
                    onDismiss = { showAddDialog = false },
                    onConfirm = { name, desc, isDestroyed ->
                        viewModel.addCustomPlanet(name, desc, isDestroyed)
                        showAddDialog = false
                    }
                )
            }
        }
    }
}

/**
 * Composable that displays a search bar for planets.
 *
 * @param query The current search query.
 * @param onQueryChange Called when the search query changes.
 * @param isFilterActive Whether the filter is active.
 * @param onFilterClick Called when the filter button is clicked.
 */
@Composable
fun PlanetSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    isFilterActive: Boolean,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("Buscar planeta") },
            modifier = Modifier.weight(1f),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onFilterClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isFilterActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isFilterActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Icon(
                imageVector = if (isFilterActive) Icons.Default.Favorite else Icons.Default.FilterList,
                contentDescription = "Filtrar Favoritos"
            )
        }
    }
}

/**
 * Composable that provides a swipe-to-delete functionality for planets.
 *
 * @param onDelete Called when the item is swiped to delete.
 * @param content The content to be displayed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanetSwipeToDeleteContainer(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color = Color.Red
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.White
                )
            }
        },
        content = { content() }
    )
}

/**
 * Composable that displays a single planet item.
 *
 * @param planet The planet to display.
 * @param onPlanetClick Called when the planet is clicked.
 * @param onFavoriteClick Called when the favorite button is clicked.
 */
@Composable
fun PlanetItem(
    planet: Planet,
    onPlanetClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onPlanetClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(planet.image)
                    .crossfade(true)
                    .build(),
                contentDescription = planet.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = planet.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = if (planet.isDestroyed) "Destruido" else "Activo",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (planet.isDestroyed) Color.Red else Color.Green
                )
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (planet.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (planet.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Composable that displays a dialog to add a new planet.
 *
 * @param onDismiss Called when the dialog is dismissed.
 * @param onConfirm Called when the confirm button is clicked.
 */
@Composable
fun AddPlanetDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isDestroyed by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Planeta") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("¿Destruido?")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(checked = isDestroyed, onCheckedChange = { isDestroyed = it })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, description, isDestroyed) },
                enabled = name.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
package com.mariogarluu.dragonballapi.ui.list

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
import com.mariogarluu.dragonballapi.data.model.Character
import com.mariogarluu.dragonballapi.ui.CharactersUiState
import com.mariogarluu.dragonballapi.ui.common.AppTopBar

/**
 * Composable that displays the list of characters.
 *
 * @param onCharacterClick Called when a character is clicked.
 * @param onUpClick Called when the up button is clicked.
 * @param viewModel The view model for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DragonBallListScreen(
    onCharacterClick: (Int) -> Unit,
    onUpClick: () -> Unit,
    viewModel: DragonBallListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isFilterActive by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                AppTopBar(
                    title = "Personajes",
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    onClick = onUpClick
                )
                SearchBar(
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
                Icon(Icons.Default.Add, contentDescription = "Añadir Personaje")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is CharactersUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is CharactersUiState.Success -> {
                    if (state.characters.isEmpty()) {
                        Text(
                            text = "No hay personajes",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(
                                items = state.characters,
                                key = { it.id }
                            ) { character ->
                                SwipeToDeleteContainer(
                                    onDelete = { viewModel.deleteCharacter(character.id) }
                                ) {
                                    CharacterItem(
                                        character = character,
                                        onCharacterClick = { onCharacterClick(character.id) },
                                        onFavoriteClick = { viewModel.toggleCharacterFavorite(character) }
                                    )
                                }
                            }
                        }
                    }
                }
                is CharactersUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            if (showAddDialog) {
                AddCharacterDialog(
                    onDismiss = { showAddDialog = false },
                    onConfirm = { name, race, ki ->
                        viewModel.addCustomCharacter(name, race, ki)
                        showAddDialog = false
                    }
                )
            }
        }
    }
}

/**
 * Composable that displays a search bar with a filter button.
 *
 * @param query The current search query.
 * @param onQueryChange Called when the search query changes.
 * @param isFilterActive Whether the filter is active.
 * @param onFilterClick Called when the filter button is clicked.
 */
@Composable
fun SearchBar(
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
            label = { Text("Buscar por nombre") },
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
 * Composable that provides a swipe-to-delete functionality.
 *
 * @param onDelete Called when the item is swiped to delete.
 * @param content The content to be displayed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteContainer(
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
 * Composable that displays a single character item.
 *
 * @param character The character to display.
 * @param onCharacterClick Called when the character is clicked.
 * @param onFavoriteClick Called when the favorite button is clicked.
 */
@Composable
fun CharacterItem(
    character: Character,
    onCharacterClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onCharacterClick),
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
                    .data(character.image)
                    .crossfade(true)
                    .build(),
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = character.name, style = MaterialTheme.typography.titleMedium)
                Text(text = character.race, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (character.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (character.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Composable that displays a dialog to add a new character.
 *
 * @param onDismiss Called when the dialog is dismissed.
 * @param onConfirm Called when the confirm button is clicked.
 */
@Composable
fun AddCharacterDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var race by remember { mutableStateOf("") }
    var ki by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Personaje") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = race,
                    onValueChange = { race = it },
                    label = { Text("Raza") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = ki,
                    onValueChange = { ki = it },
                    label = { Text("Ki") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, race, ki) },
                enabled = name.isNotBlank() && race.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
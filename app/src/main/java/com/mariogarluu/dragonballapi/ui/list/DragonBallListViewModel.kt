package com.mariogarluu.dragonballapi.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariogarluu.dragonballapi.data.model.Character
import com.mariogarluu.dragonballapi.data.repo.DragonBallRepository
import com.mariogarluu.dragonballapi.ui.CharactersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DragonBallListViewModel @Inject constructor(
    private val repository: DragonBallRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _isFavoriteFilterActive = MutableStateFlow(false)
    private val _networkError = MutableStateFlow<String?>(null)

    /**
     * Estado reactivo de la UI.
     * Combina la Única Fuente de Verdad (Base de Datos) con el estado de la UI (Búsqueda, Filtros).
     * Esto asegura que la lista se actualice automáticamente cuando la BD cambia.
     */
    val uiState: StateFlow<CharactersUiState> = combine(
        repository.getCharacters(),
        _searchQuery,
        _isFavoriteFilterActive,
        _networkError
    ) { characters, query, onlyFavorites, error ->

        val filteredList = characters.filter { character ->
            val matchesName = character.name.contains(query, ignoreCase = true)
            val matchesFavorite = if (onlyFavorites) character.isFavorite else true
            matchesName && matchesFavorite
        }

        when {
            // Caso 1: Error crítico (Lista vacía y error de red)
            characters.isEmpty() && error != null -> CharactersUiState.Error(error)

            // Caso 2: Cargando inicial (Lista vacía y sin error aún)
            characters.isEmpty() && error == null -> CharactersUiState.Loading

            // Caso 3: Éxito (Tenemos datos, vengan de caché o red)
            else -> CharactersUiState.Success(filteredList)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CharactersUiState.Loading
    )

    init {
        refreshCharacters()
    }

    fun refreshCharacters() {
        viewModelScope.launch {
            _networkError.value = null
            // refreshCharacters no devuelve datos, solo actualiza la DB.
            // La propiedad 'uiState' arriba reaccionará automáticamente a los cambios en DB.
            val result = repository.refreshCharacters()

            if (result.isFailure) {
                _networkError.value = result.exceptionOrNull()?.message ?: "Error de conexión"
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavoriteFilter() {
        _isFavoriteFilterActive.value = !_isFavoriteFilterActive.value
    }

    fun toggleCharacterFavorite(character: Character) {
        viewModelScope.launch {
            repository.toggleFavorite(character.id, !character.isFavorite)
        }
    }

    fun deleteCharacter(characterId: Int) {
        viewModelScope.launch {
            repository.deleteCharacter(characterId)
        }
    }

    fun addCustomCharacter(name: String, race: String, ki: String) {
        viewModelScope.launch {
            val newId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
            val newCharacter = Character(
                id = newId,
                name = name,
                race = race,
                ki = ki,
                maxKi = "Desconocido",
                gender = "Desconocido",
                description = "Personaje creado localmente",
                image = "https://dragonball-api.com/characters/goku_normal.png",
                affiliation = "Custom",
                originPlanet = null,
                transformations = emptyList(),
                isFavorite = false
            )
            repository.addLocalCharacter(newCharacter)
        }
    }
}
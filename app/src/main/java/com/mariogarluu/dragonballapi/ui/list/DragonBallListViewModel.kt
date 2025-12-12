package com.mariogarluu.dragonballapi.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariogarluu.dragonballapi.Data.Model.Character
import com.mariogarluu.dragonballapi.Data.Repo.DragonBallRepository
import com.mariogarluu.dragonballapi.ui.CharactersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DragonBallListViewModel @Inject constructor(
    private val repository: DragonBallRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _isFavoriteFilterActive = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<CharactersUiState> = combine(
        repository.getCharacters(),
        _searchQuery,
        _isFavoriteFilterActive,
        _isLoading,
        _errorMessage
    ) { characters, query, onlyFavorites, isLoading, error ->
        
        val filteredList = characters.filter { character ->
            val matchesName = character.name.contains(query, ignoreCase = true)
            val matchesFavorite = if (onlyFavorites) character.isFavorite else true
            matchesName && matchesFavorite
        }

        when {
            isLoading && characters.isEmpty() -> CharactersUiState.Loading
            error != null && characters.isEmpty() -> CharactersUiState.Error(error)
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
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.refreshCharacters()
            if (result.isFailure) {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Error desconocido"
            }
            _isLoading.value = false
        }
    }

    // --- FUNCIONES CON NOMBRES LARGOS (COMO PIDE LA UI) ---

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
            // ID aleatorio local
            val newId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
            val newCharacter = Character(
                id = newId,
                name = name,
                race = race,
                ki = ki,
                maxKi = "Desconocido",
                gender = "Desconocido",
                description = "Personaje creado localmente",
                image = "https://dragonball-api.com/characters/goku_normal.png", // Placeholder
                affiliation = "Custom",
                originPlanet = null,
                transformations = emptyList(),
                isFavorite = false
            )
            repository.addLocalCharacter(newCharacter)
        }
    }
}
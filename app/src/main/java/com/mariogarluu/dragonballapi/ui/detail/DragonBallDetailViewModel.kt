package com.mariogarluu.dragonballapi.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariogarluu.dragonballapi.Data.Model.Character
import com.mariogarluu.dragonballapi.Data.Repo.DragonBallRepository
import com.mariogarluu.dragonballapi.ui.CharacterDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the character detail screen.
 *
 * @property repository The repository for fetching character data.
 * @param savedStateHandle Handle to the saved state of the view.
 */
@HiltViewModel
class DragonBallDetailViewModel @Inject constructor(
    private val repository: DragonBallRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<CharacterDetailUiState>(CharacterDetailUiState.Loading)
    /**
     * The UI state for the character detail screen.
     */
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    private val characterId: Int = checkNotNull(savedStateHandle["characterId"])

    init {
        loadCharacter()
    }

    /**
     * Loads the character from the repository and updates the UI state.
     */
    private fun loadCharacter() {
        viewModelScope.launch {
            repository.refreshCharacterById(characterId)
            repository.getCharacterById(characterId).collect { character ->
                if (character != null) {
                    _uiState.value = CharacterDetailUiState.Success(character)
                } else {
                    _uiState.value = CharacterDetailUiState.Error("Personaje no encontrado")
                }
            }
        }
    }

    /**
     * Toggles the favorite status of a character.
     *
     * @param character The character to toggle the favorite status for.
     */
    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            repository.toggleFavorite(character.id, !character.isFavorite)
        }
    }
}
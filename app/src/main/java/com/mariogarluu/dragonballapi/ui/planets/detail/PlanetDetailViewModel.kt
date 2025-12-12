package com.mariogarluu.dragonballapi.ui.planets.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariogarluu.dragonballapi.Data.Model.Planet
import com.mariogarluu.dragonballapi.Data.Repo.DragonBallRepository
import com.mariogarluu.dragonballapi.ui.PlanetDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the planet detail screen.
 *
 * @property repository The repository for fetching planet data.
 * @param savedStateHandle Handle to the saved state of the view.
 */
@HiltViewModel
class PlanetDetailViewModel @Inject constructor(
    private val repository: DragonBallRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlanetDetailUiState>(PlanetDetailUiState.Loading)
    /**
     * The UI state for the planet detail screen.
     */
    val uiState: StateFlow<PlanetDetailUiState> = _uiState.asStateFlow()

    private val planetId: Int = checkNotNull(savedStateHandle["planetId"])

    init {
        loadPlanet()
    }

    /**
     * Loads the planet from the repository and updates the UI state.
     */
    private fun loadPlanet() {
        viewModelScope.launch {
            repository.refreshPlanetById(planetId)
            repository.getPlanetById(planetId).collect { planet ->
                if (planet != null) {
                    _uiState.value = PlanetDetailUiState.Success(planet)
                } else {
                    _uiState.value = PlanetDetailUiState.Error("Planeta no encontrado")
                }
            }
        }
    }

    /**
     * Toggles the favorite status of a planet.
     *
     * @param planet The planet to toggle the favorite status for.
     */
    fun toggleFavorite(planet: Planet) {
        viewModelScope.launch {
            repository.togglePlanetFavorite(planet.id, !planet.isFavorite)
        }
    }
}
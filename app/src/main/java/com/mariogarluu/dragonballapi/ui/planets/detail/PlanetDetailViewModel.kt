package com.mariogarluu.dragonballapi.ui.planets.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariogarluu.dragonballapi.data.model.Planet
import com.mariogarluu.dragonballapi.data.repo.DragonBallRepository
import com.mariogarluu.dragonballapi.ui.PlanetDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanetDetailViewModel @Inject constructor(
    private val repository: DragonBallRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val planetId: Int = checkNotNull(savedStateHandle["planetId"])

    val uiState: StateFlow<PlanetDetailUiState> = repository.getPlanetById(planetId)
        .map { planet ->
            if (planet != null) {
                PlanetDetailUiState.Success(planet)
            } else {
                PlanetDetailUiState.Error("Planeta no encontrado")
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlanetDetailUiState.Loading
        )

    init {
        refreshPlanet()
    }

    private fun refreshPlanet() {
        viewModelScope.launch {
            repository.refreshPlanetById(planetId)
        }
    }

    fun toggleFavorite(planet: Planet) {
        viewModelScope.launch {
            repository.togglePlanetFavorite(planet.id, !planet.isFavorite)
        }
    }
}
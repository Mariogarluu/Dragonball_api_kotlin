package com.mariogarluu.dragonballapi.ui.planets.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariogarluu.dragonballapi.Data.Model.Planet
import com.mariogarluu.dragonballapi.Data.Repo.DragonBallRepository
import com.mariogarluu.dragonballapi.ui.PlanetsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the planet list screen.
 *
 * @property repository The repository for fetching planet data.
 */
@HiltViewModel
class PlanetListViewModel @Inject constructor(
    private val repository: DragonBallRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _isFavoriteFilterActive = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(false)

    /**
     * The UI state for the planet list screen.
     */
    val uiState: StateFlow<PlanetsUiState> = combine(
        repository.getPlanets(),
        _searchQuery,
        _isFavoriteFilterActive,
        _isLoading
    ) { planets, query, onlyFavorites, isLoading ->
        
        val filteredList = planets.filter { planet ->
            val matchesName = planet.name.contains(query, ignoreCase = true)
            val matchesFavorite = if (onlyFavorites) planet.isFavorite else true
            matchesName && matchesFavorite
        }

        if (isLoading && planets.isEmpty()) {
            PlanetsUiState.Loading
        } else {
            PlanetsUiState.Success(filteredList)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlanetsUiState.Loading
    )

    init {
        refreshPlanets()
    }

    /**
     * Refreshes the list of planets from the repository.
     */
    fun refreshPlanets() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.refreshPlanets()
            _isLoading.value = false
        }
    }

    /**
     * Called when the search query changes.
     *
     * @param query The new search query.
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    /**
     * Toggles the favorite filter.
     */
    fun toggleFavoriteFilter() {
        _isFavoriteFilterActive.value = !_isFavoriteFilterActive.value
    }

    /**
     * Toggles the favorite status of a planet.
     *
     * @param planet The planet to toggle the favorite status for.
     */
    fun togglePlanetFavorite(planet: Planet) {
        viewModelScope.launch {
            repository.togglePlanetFavorite(planet.id, !planet.isFavorite)
        }
    }

    /**
     * Deletes a planet.
     *
     * @param planetId The ID of the planet to delete.
     */
    fun deletePlanet(planetId: Int) {
        viewModelScope.launch {
            repository.deletePlanet(planetId)
        }
    }

    /**
     * Adds a new custom planet.
     *
     * @param name The name of the planet.
     * @param description A description of the planet.
     * @param isDestroyed Whether the planet is destroyed.
     */
    fun addCustomPlanet(name: String, description: String, isDestroyed: Boolean) {
        viewModelScope.launch {
            val newId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
            val newPlanet = Planet(
                id = newId,
                name = name,
                isDestroyed = isDestroyed,
                description = description,
                image = "https://dragonball-api.com/planets/kaio.png", // Placeholder
                isFavorite = false
            )
            repository.addLocalPlanet(newPlanet)
        }
    }
}
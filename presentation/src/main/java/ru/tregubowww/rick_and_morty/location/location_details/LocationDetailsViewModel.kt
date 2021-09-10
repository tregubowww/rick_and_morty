package ru.tregubowww.rick_and_morty.location.location_details

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tregubowww.domain.entity.Location
import ru.tregubowww.domain.interactor.CharactersInteractor
import ru.tregubowww.domain.interactor.LocationInteractor
import ru.tregubowww.rick_and_morty.state.State
import ru.tregubowww.domain.entity.Character as Character

class LocationDetailsViewModel(
    private val locationId: Long,
    private val locationInteractor: LocationInteractor,
    private val charactersInteractor: CharactersInteractor,
    val locationDetailsAdapter: LocationDetailsAdapter
) : ViewModel() {

    private val _mutableLoadingState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableLoadingState.asStateFlow()

    private val _mutableCharacterList = MutableStateFlow<List<Character>>(emptyList())
    val mutableCharacterList: StateFlow<List<Character>> get() = _mutableCharacterList.asStateFlow()

    private val _mutableLocationDetails = MutableStateFlow<Location?>(null)
    val mutableLocationDetails: StateFlow<Location?> get() = _mutableLocationDetails.asStateFlow()

    fun refreshLocationDetails() {
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                val location = locationInteractor.getLocationFromNetwork(locationId)
                _mutableLocationDetails.emit(location)
                _mutableCharacterList.emit(charactersInteractor.getCharacterListById (location.residents))
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
                val location = locationInteractor.getLocationFromDb(locationId)
                _mutableLocationDetails.emit(location)
                _mutableCharacterList.emit(charactersInteractor.getCharacterListByIdFromDb(location.residents))
            }
        }
    }

    init {
        refreshLocationDetails()
    }

}

class LocationDetailsViewModelFactory @AssistedInject constructor(
    @Assisted("locationId") private val locationId: Long,
    private val charactersInteractor: CharactersInteractor,
    private val locationsInteractor: LocationInteractor,
    private val locationDetailsAdapter: LocationDetailsAdapter,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        LocationDetailsViewModel(locationId, locationsInteractor, charactersInteractor, locationDetailsAdapter) as T

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("locationId") locationId: Long): LocationDetailsViewModelFactory
    }
}
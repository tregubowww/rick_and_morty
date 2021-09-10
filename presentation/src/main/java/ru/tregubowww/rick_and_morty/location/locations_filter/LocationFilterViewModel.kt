package ru.tregubowww.rick_and_morty.location.locations_filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.tregubowww.domain.entity.Location
import ru.tregubowww.domain.interactor.LocationInteractor
import ru.tregubowww.domain.query.FilterQueryLocation
import ru.tregubowww.rick_and_morty.character.LoaderStateAdapter
import ru.tregubowww.rick_and_morty.location.LocationsAdapter
import ru.tregubowww.rick_and_morty.state.State

class LocationFilterViewModel(
    private val filterQueryLocation: FilterQueryLocation,
    private val locationInteractor: LocationInteractor,
    val locationsAdapter: LocationsAdapter,
    val loaderStateAdapter: LoaderStateAdapter,
) : ViewModel() {

    private val _mutableState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableState.asStateFlow()

    private val _mutableFilterLocationsFromDb = MutableStateFlow<List<Location>>(emptyList())
    val mutableFilterLocationsFromDb: StateFlow<List<Location>> get() = _mutableFilterLocationsFromDb.asStateFlow()

    private val _queryFilterLocation = MutableSharedFlow<FilterQueryLocation>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val queryFilterLocation: SharedFlow<FilterQueryLocation> get() = _queryFilterLocation.asSharedFlow()

    private var newPagingSource: PagingSource<*, *>? = null

    val locationsFlow: Flow<PagingData<Location>> = queryFilterLocation
        .map(::newPager)
        .flatMapLatest { pager -> pager.flow }
        .cachedIn(viewModelScope)

    private fun newPager(queryFilterLocation: FilterQueryLocation): Pager<Int, Location> {
        return Pager(PagingConfig(DEFAULT_PAGE_SIZE, enablePlaceholders = false)) {
            newPagingSource?.invalidate()
            locationInteractor.filterLocations(queryFilterLocation).also { newPagingSource = it }
        }
    }

    private fun adapterLoadStateListener() {
        locationsAdapter.addLoadStateListener { state ->
            viewModelScope.launch {
                if (state.refresh is LoadState.Loading) _mutableState.emit(State.Loading)
                if (state.refresh is LoadState.NotLoading) _mutableState.emit(State.NotLoading)
                if (state.append is LoadState.Error || state.refresh is LoadState.Error) {
                    _mutableState.emit(State.Error)
                    _mutableFilterLocationsFromDb.emit(locationInteractor.filterLocationsFromDb(filterQueryLocation))
                }
            }
        }
    }

    fun refreshLocations() {
        viewModelScope.launch {
            _queryFilterLocation.emit(filterQueryLocation)
        }
    }

    init {
        refreshLocations()
        adapterLoadStateListener()
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }
}

class LocationFilterViewModelFactory @AssistedInject constructor(
    @Assisted("filterData") private val filterQueryLocation: FilterQueryLocation,
    private val locationInteractor: LocationInteractor,
    val locationsAdapter: LocationsAdapter,
    val loaderStateAdapter: LoaderStateAdapter,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        LocationFilterViewModel(filterQueryLocation, locationInteractor, locationsAdapter, loaderStateAdapter) as T

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("filterData") filterQueryLocation: FilterQueryLocation): LocationFilterViewModelFactory
    }
}
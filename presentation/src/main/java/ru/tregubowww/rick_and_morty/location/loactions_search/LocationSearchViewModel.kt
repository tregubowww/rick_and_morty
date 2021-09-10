package ru.tregubowww.rick_and_morty.location.loactions_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.tregubowww.domain.entity.Location
import ru.tregubowww.domain.interactor.LocationInteractor
import ru.tregubowww.rick_and_morty.character.LoaderStateAdapter
import ru.tregubowww.rick_and_morty.location.LocationsAdapter
import ru.tregubowww.rick_and_morty.state.State
import javax.inject.Inject

class LocationSearchViewModel @Inject constructor(
    private val locationInteractor: LocationInteractor,
    val locationsAdapter: LocationsAdapter,
    val loaderStateAdapter: LoaderStateAdapter,
) : ViewModel() {

    private val _mutableState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableState.asStateFlow()

    private val _mutableSearchLocationsFromDb = MutableStateFlow<List<Location>>(emptyList())
    val mutableSearchLocationsFromDb: StateFlow<List<Location>> get() = _mutableSearchLocationsFromDb.asStateFlow()

    private val _querySearch = MutableStateFlow("")
    val querySearch: StateFlow<String> = _querySearch.asStateFlow()

    private var newPagingSource: PagingSource<*, *>? = null

    val locationsFlow: Flow<PagingData<Location>> = querySearch
        .debounce(DEBOUNCE_TIME)
        .map(::newPager)
        .flatMapLatest { pager -> pager.flow }
        .cachedIn(viewModelScope)

    private fun newPager(query: String): Pager<Int, Location> {
        return Pager(PagingConfig(DEFAULT_PAGE_SIZE, enablePlaceholders = false)) {
            newPagingSource?.invalidate()
            locationInteractor.searchLocation(query).also { newPagingSource = it }
        }
    }

    fun setQuery(query: String) {
        _querySearch.tryEmit(query)
    }

    private fun adapterLoadStateListener() {
        locationsAdapter.addLoadStateListener { state ->

            viewModelScope.launch {
                if (state.refresh is LoadState.Loading) _mutableState.emit(State.Loading)
                if (state.refresh is LoadState.NotLoading) _mutableState.emit(State.NotLoading)
                if (state.append is LoadState.Error || state.refresh is LoadState.Error) {
                    _mutableState.emit(State.Error)
                    locationsAdapter.submitData(PagingData.empty())
                    _mutableSearchLocationsFromDb.
                    emit(locationInteractor.searchLocationFromDb(querySearch.value))
                }
            }
        }
    }

    fun refresh() {
        _querySearch.tryEmit(querySearch.value)
    }

    init {
        adapterLoadStateListener()
    }

    companion object{
        private const val DEBOUNCE_TIME = 500L
        private const val DEFAULT_PAGE_SIZE = 20
    }

}
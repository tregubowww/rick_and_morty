package ru.tregubowww.rick_and_morty.location.locations

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
    import kotlinx.coroutines.flow.flatMapLatest
    import kotlinx.coroutines.launch
    import ru.tregubowww.domain.entity.Location
    import ru.tregubowww.domain.interactor.LocationInteractor
    import ru.tregubowww.rick_and_morty.character.LoaderStateAdapter
    import ru.tregubowww.rick_and_morty.location.LocationsAdapter
    import ru.tregubowww.rick_and_morty.state.State
    import javax.inject.Inject

class LocationsViewModel
    @Inject constructor(
        private val locationInteractor: LocationInteractor,
        val locationsAdapter: LocationsAdapter,
        val loaderStateAdapter: LoaderStateAdapter,
    ) : ViewModel() {

        private val _mutableState = MutableStateFlow<State>(State.Loading)
        val mutableState: StateFlow<State> get() = _mutableState.asStateFlow()

        private val _mutablePager = MutableStateFlow(newPager())
        private val mutablePager: StateFlow<Pager<Int, Location>> get() = _mutablePager.asStateFlow()

        private val _mutableLocationsFromDb = MutableStateFlow<List<Location>>(emptyList())
        val mutableLocationsFromDb: StateFlow<List<Location>> get() = _mutableLocationsFromDb.asStateFlow()

        private var newPagingSource: PagingSource<*, *>? = null

        val locationsFlow: Flow<PagingData<Location>> = mutablePager
            .flatMapLatest { pager -> pager.flow }
            .cachedIn(viewModelScope)

        private fun newPager(): Pager<Int, Location> {

            return Pager(PagingConfig(DEFAULT_PAGE_SIZE, enablePlaceholders = false)) {
                newPagingSource?.invalidate()
                locationInteractor.getAllLocations().also { newPagingSource = it }
            }
        }

        private fun adapterLoadStateListener() {
            locationsAdapter.addLoadStateListener { state ->

                viewModelScope.launch {
                    if (state.refresh is LoadState.Loading) _mutableState.emit(State.Loading)
                    if (state.refresh is LoadState.NotLoading) _mutableState.emit(State.NotLoading)
                    if (state.append is LoadState.Error || state.refresh is LoadState.Error) {
                        _mutableState.emit(State.Error)
                        _mutableLocationsFromDb.emit(locationInteractor.getLocationListFromDb())
                    }
                }
            }
        }

        fun refreshLocations() {
            _mutablePager.tryEmit(newPager())
        }

        init {
            adapterLoadStateListener()
        }

        companion object {
            private const val DEFAULT_PAGE_SIZE = 20
        }
    }

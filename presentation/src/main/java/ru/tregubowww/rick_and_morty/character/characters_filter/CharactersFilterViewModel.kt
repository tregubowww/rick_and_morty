package ru.tregubowww.rick_and_morty.character.characters_filter

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
import ru.tregubowww.domain.interactor.CharactersInteractor
import ru.tregubowww.domain.query.FilterQueryCharacter
import ru.tregubowww.rick_and_morty.character.CharactersAdapter
import ru.tregubowww.rick_and_morty.character.LoaderStateAdapter
import ru.tregubowww.rick_and_morty.state.State
import ru.tregubowww.domain.entity.Character

class CharactersFilterViewModel(
    private val filterQueryCharacter: FilterQueryCharacter,
    private val charactersInteractor: CharactersInteractor,
    val charactersAdapter: CharactersAdapter,
    val loaderStateAdapter: LoaderStateAdapter,
) : ViewModel() {

    private val _mutableState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableState.asStateFlow()

    private val _mutableFilterCharactersFromDb = MutableStateFlow<List<Character>>(emptyList())
    val mutableFilterCharactersFromDb: StateFlow<List<Character>> get() = _mutableFilterCharactersFromDb.asStateFlow()

    private val _queryFilter = MutableSharedFlow<FilterQueryCharacter>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val queryFilterCharacter: SharedFlow<FilterQueryCharacter> get() = _queryFilter.asSharedFlow()

    private var newPagingSource: PagingSource<*, *>? = null

    val charactersFlow: Flow<PagingData<Character>> = queryFilterCharacter
        .map(::newPager)
        .flatMapLatest { pager -> pager.flow }
        .cachedIn(viewModelScope)

    private fun newPager(queryFilterCharacter: FilterQueryCharacter): Pager<Int, Character> {
        return Pager(PagingConfig(DEFAULT_PAGE_SIZE, enablePlaceholders = false)) {
            newPagingSource?.invalidate()
            charactersInteractor.filterCharacters(queryFilterCharacter).also { newPagingSource = it }
        }
    }

    private fun adapterLoadStateListener() {
        charactersAdapter.addLoadStateListener { state ->
            viewModelScope.launch {
                if (state.refresh is LoadState.Loading) _mutableState.emit(State.Loading)
                if (state.refresh is LoadState.NotLoading) _mutableState.emit(State.NotLoading)
                if (state.append is LoadState.Error || state.refresh is LoadState.Error) {
                    _mutableState.emit(State.Error)
                    _mutableFilterCharactersFromDb.emit(charactersInteractor.filterCharactersFromDb(filterQueryCharacter))
                }
            }
        }
    }

    fun refreshCharacters() {
        viewModelScope.launch {
            _queryFilter.emit(filterQueryCharacter)
        }
    }

    init {
        refreshCharacters()
        adapterLoadStateListener()
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }
}

class CharactersFilterViewModelFactory @AssistedInject constructor(
    @Assisted("filterData") private val filterQueryCharacter: FilterQueryCharacter,
    private val charactersInteractor: CharactersInteractor,
    val charactersAdapter: CharactersAdapter,
    val loaderStateAdapter: LoaderStateAdapter,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CharactersFilterViewModel(filterQueryCharacter, charactersInteractor, charactersAdapter, loaderStateAdapter) as T

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("filterData") filterQueryCharacter: FilterQueryCharacter): CharactersFilterViewModelFactory
    }
}
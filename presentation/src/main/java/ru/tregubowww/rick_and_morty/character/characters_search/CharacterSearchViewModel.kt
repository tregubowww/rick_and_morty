package ru.tregubowww.rick_and_morty.character.characters_search

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
import ru.tregubowww.domain.interactor.CharactersInteractor
import ru.tregubowww.rick_and_morty.character.CharactersAdapter
import ru.tregubowww.rick_and_morty.character.LoaderStateAdapter
import ru.tregubowww.rick_and_morty.state.State
import javax.inject.Inject
import ru.tregubowww.domain.entity.Character

class CharacterSearchViewModel @Inject constructor(
    private val charactersInteractor: CharactersInteractor,
    val charactersAdapter: CharactersAdapter,
    val loaderStateAdapter: LoaderStateAdapter,
) : ViewModel() {

    private val _mutableState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableState.asStateFlow()

    private val _mutableSearchCharactersFromDb = MutableStateFlow<List<Character>>(emptyList())
    val mutableSearchCharactersFromDb: StateFlow<List<Character>> get() = _mutableSearchCharactersFromDb.asStateFlow()

    private val _querySearch = MutableStateFlow("")
    val querySearch: StateFlow<String> = _querySearch.asStateFlow()

    private var newPagingSource: PagingSource<*, *>? = null

    val charactersFlow: Flow<PagingData<Character>> = querySearch
        .debounce(DEBOUNCE_TIME)
        .map(::newPager)
        .flatMapLatest { pager -> pager.flow }
        .cachedIn(viewModelScope)

    private fun newPager(query: String): Pager<Int, Character> {
        return Pager(PagingConfig(DEFAULT_PAGE_SIZE, enablePlaceholders = false)) {
            newPagingSource?.invalidate()
            charactersInteractor.searchCharacter(query).also { newPagingSource = it }
        }
    }

    fun setQuery(query: String) {
        _querySearch.tryEmit(query)
    }

    private fun adapterLoadStateListener() {
        charactersAdapter.addLoadStateListener { state ->

            viewModelScope.launch {
                if (state.refresh is LoadState.Loading) _mutableState.emit(State.Loading)
                if (state.refresh is LoadState.NotLoading) _mutableState.emit(State.NotLoading)
                if (state.append is LoadState.Error || state.refresh is LoadState.Error) {
                    _mutableState.emit(State.Error)
                    charactersAdapter.submitData(PagingData.empty())
                    _mutableSearchCharactersFromDb.
                    emit(charactersInteractor.searchCharacterFromDb(querySearch.value))
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
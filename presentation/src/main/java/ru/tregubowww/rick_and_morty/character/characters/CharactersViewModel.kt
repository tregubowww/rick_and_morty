package ru.tregubowww.rick_and_morty.character.characters

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
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.domain.interactor.CharactersInteractor
import ru.tregubowww.rick_and_morty.character.CharactersAdapter
import ru.tregubowww.rick_and_morty.character.LoaderStateAdapter
import ru.tregubowww.rick_and_morty.state.State
import javax.inject.Inject

class CharactersViewModel
@Inject constructor(
    private val charactersInteractor: CharactersInteractor,
    val adapterCharacters: CharactersAdapter,
    val loaderStateAdapter: LoaderStateAdapter,
) : ViewModel() {

    private val _mutableState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableState.asStateFlow()

    private val _mutablePager = MutableStateFlow(newPager())
    private val mutablePager: StateFlow<Pager<Int, Character>> get() = _mutablePager.asStateFlow()

    private val _mutableCharactersFromDb = MutableStateFlow<List<Character>>(emptyList())
    val mutableCharactersFromDb: StateFlow<List<Character>> get() = _mutableCharactersFromDb.asStateFlow()

    private var newPagingSource: PagingSource<*, *>? = null

    val charactersFlow: Flow<PagingData<Character>> = mutablePager
        .flatMapLatest { pager -> pager.flow }
        .cachedIn(viewModelScope)

    private fun newPager(): Pager<Int, Character> {

        return Pager(PagingConfig(DEFAULT_PAGE_SIZE, enablePlaceholders = false)) {
            newPagingSource?.invalidate()
            charactersInteractor.getAllCharacters().also { newPagingSource = it }
        }
    }

    private fun adapterLoadStateListener() {
        adapterCharacters.addLoadStateListener { state ->
            viewModelScope.launch {
                if (state.refresh is LoadState.Loading) _mutableState.emit(State.Loading)
                if (state.refresh is LoadState.NotLoading) _mutableState.emit(State.NotLoading)
                if (state.append is LoadState.Error || state.refresh is LoadState.Error) {
                    _mutableState.emit(State.Error)
                    _mutableCharactersFromDb.emit(charactersInteractor.getCharacterListFromDb())
                }
            }
        }
    }

    fun refreshCharacters() {
        _mutablePager.tryEmit(newPager())
    }

    init {
        adapterLoadStateListener()
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }
}
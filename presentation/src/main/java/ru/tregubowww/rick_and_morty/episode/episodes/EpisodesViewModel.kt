package ru.tregubowww.rick_and_morty.episode.episodes

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
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.domain.interactor.EpisodesInteractor
import ru.tregubowww.rick_and_morty.character.LoaderStateAdapter
import ru.tregubowww.rick_and_morty.episode.EpisodeAdapter
import ru.tregubowww.rick_and_morty.state.State
import javax.inject.Inject

class EpisodesViewModel
@Inject constructor(
    private val episodesInteractor: EpisodesInteractor,
    val episodesAdapter: EpisodeAdapter,
    val loaderStateAdapter: LoaderStateAdapter,
) : ViewModel() {

    private val _mutableState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableState.asStateFlow()

    private val _mutablePager = MutableStateFlow(newPager())
    private val mutablePager: StateFlow<Pager<Int, Episode>> get() = _mutablePager.asStateFlow()

    private val _mutableEpisodesFromDb = MutableStateFlow<List<Episode>>(emptyList())
    val mutableEpisodesFromDb: StateFlow<List<Episode>> get() = _mutableEpisodesFromDb.asStateFlow()

    private var newPagingSource: PagingSource<*, *>? = null

    val episodesFlow: Flow<PagingData<Episode>> = mutablePager
        .flatMapLatest { pager -> pager.flow }
        .cachedIn(viewModelScope)

    private fun newPager(): Pager<Int, Episode> {

        return Pager(PagingConfig(DEFAULT_PAGE_SIZE, enablePlaceholders = false)) {
            newPagingSource?.invalidate()
            episodesInteractor.getAllEpisodes().also { newPagingSource = it }
        }
    }

    private fun adapterLoadStateListener() {
        episodesAdapter.addLoadStateListener { state ->

            viewModelScope.launch {
                if (state.refresh is LoadState.Loading) _mutableState.emit(State.Loading)
                if (state.refresh is LoadState.NotLoading) _mutableState.emit(State.NotLoading)
                if (state.append is LoadState.Error || state.refresh is LoadState.Error) {
                    _mutableState.emit(State.Error)
                    _mutableEpisodesFromDb.emit(episodesInteractor.getEpisodeListFromDb())
                }
            }
        }
    }

    fun refreshEpisodes() {
        _mutablePager.tryEmit(newPager())
    }

    init {
        adapterLoadStateListener()
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }
}
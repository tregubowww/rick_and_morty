package ru.tregubowww.rick_and_morty.episode.episode_details

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
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.domain.interactor.CharactersInteractor
import ru.tregubowww.domain.interactor.EpisodesInteractor
import ru.tregubowww.rick_and_morty.character.character_details.EpisodeDetailsAdapter
import ru.tregubowww.rick_and_morty.state.State
import ru.tregubowww.domain.entity.Character as Character

class EpisodeDetailsViewModel(
    private val episodeId: Long,
    private val episodesInteractor: EpisodesInteractor,
    private val charactersInteractor: CharactersInteractor,
    val episodeDetailsAdapter: EpisodeDetailsAdapter
) : ViewModel() {

    private val _mutableLoadingState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableLoadingState.asStateFlow()

    private val _mutableCharacterList = MutableStateFlow<List<Character>>(emptyList())
    val mutableCharacterList: StateFlow<List<Character>> get() = _mutableCharacterList.asStateFlow()

    private val _mutableEpisodeDetails = MutableStateFlow<Episode?>(null)
    val mutableEpisodeDetails: StateFlow<Episode?> get() = _mutableEpisodeDetails.asStateFlow()

    fun refreshEpisodeDetails() {
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                val episode = episodesInteractor.getEpisodeFromNetwork(episodeId)
                _mutableEpisodeDetails.emit(episode)
                _mutableCharacterList.emit(charactersInteractor.getCharacterListById (episode.characters))
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
                val Episode = episodesInteractor.getEpisodeFromDb(episodeId)
                _mutableEpisodeDetails.emit(Episode)
                _mutableCharacterList.emit(charactersInteractor.getCharacterListByIdFromDb(Episode.characters))
            }
        }
    }

    init {
        refreshEpisodeDetails()
    }

}

class EpisodeDetailsViewModelFactory @AssistedInject constructor(
    @Assisted("episodeId") private val episodeId: Long,
    private val charactersInteractor: CharactersInteractor,
    private val episodesInteractor: EpisodesInteractor,
    private val episodeDetailsAdapter: EpisodeDetailsAdapter,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        EpisodeDetailsViewModel(episodeId, episodesInteractor, charactersInteractor, episodeDetailsAdapter) as T

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("episodeId") EpisodeId: Long): EpisodeDetailsViewModelFactory
    }
}
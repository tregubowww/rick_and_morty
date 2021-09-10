package ru.tregubowww.rick_and_morty.character.character_details

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
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.domain.interactor.CharactersInteractor
import ru.tregubowww.domain.interactor.EpisodesInteractor
import ru.tregubowww.rick_and_morty.state.State

class CharacterDetailsViewModel(
    private val characterId: Long,
    private val episodesInteractor: EpisodesInteractor,
    private val charactersInteractor: CharactersInteractor,
    val characterDetailsAdapter: CharacterDetailsAdapter,
) : ViewModel() {

    private val _mutableLoadingState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableLoadingState.asStateFlow()

    private val _mutableEpisodeList = MutableStateFlow<List<Episode>>(emptyList())
    val mutableEpisodeList: StateFlow<List<Episode>> get() = _mutableEpisodeList.asStateFlow()

    private val _mutableCharacterDetails = MutableStateFlow<Character?>(null)
    val mutableCharacterDetails: StateFlow<Character?> get() = _mutableCharacterDetails.asStateFlow()

    fun refreshCharacterDetails() {
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                val character = charactersInteractor.getCharacterFromNetwork(characterId)
                _mutableCharacterDetails.emit(character)
                _mutableEpisodeList.emit(episodesInteractor.getEpisodeListById(character.episodesId))
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
                val character = charactersInteractor.getCharacterFromDb(characterId)
                _mutableCharacterDetails.emit(character)
                _mutableEpisodeList.emit(episodesInteractor.getEpisodeListByIdFromDb(character.episodesId))
            }
        }
    }

    init {
        refreshCharacterDetails()
    }

}

class CharacterDetailsViewModelFactory @AssistedInject constructor(
    @Assisted("characterId") private val characterId: Long,
    private val episodesInteractor: EpisodesInteractor,
    private val charactersInteractor: CharactersInteractor,
    private val characterDetailsAdapter: CharacterDetailsAdapter,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CharacterDetailsViewModel(characterId, episodesInteractor, charactersInteractor, characterDetailsAdapter) as T

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("characterId") characterId: Long): CharacterDetailsViewModelFactory
    }
}
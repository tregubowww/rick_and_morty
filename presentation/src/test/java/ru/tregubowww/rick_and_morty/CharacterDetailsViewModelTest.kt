package ru.tregubowww.rick_and_morty

import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Test
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.domain.interactor.CharactersInteractor
import ru.tregubowww.domain.interactor.EpisodesInteractor
import ru.tregubowww.rick_and_morty.character.character_details.CharacterDetailsViewModel

class CharacterDetailsViewModelTest {

    @Test
    fun get_character_details_with_network_error() {
        runBlocking {
            val charactersInteractor = mockk<CharactersInteractor> {
                coEvery { getCharacterFromNetwork(FAKE_CHARACTER_ID) } throws NetworkException()
                coEvery { getCharacterFromDb(FAKE_CHARACTER_ID) } returns createCharacterDetailsTest()
            }

            val episodesInteractor = mockk<EpisodesInteractor> {
                coEvery { getEpisodeListById(FAKE_EPISODES_ID) } throws NetworkException()
                coEvery { getEpisodeListByIdFromDb(FAKE_EPISODES_ID) } returns createEpisodesListTest()
            }


            CharacterDetailsViewModel(FAKE_CHARACTER_ID, episodesInteractor, charactersInteractor, mockk())
            coVerify {
                charactersInteractor.getCharacterFromDb(FAKE_CHARACTER_ID)
                episodesInteractor.getEpisodeListByIdFromDb(FAKE_EPISODES_ID)

            }
        }

    }

    @Test
    fun get_character_details_without_network_error() {
        runBlocking {
            val charactersInteractor = mockk<CharactersInteractor> {
                coEvery { getCharacterFromNetwork(FAKE_CHARACTER_ID) } returns createCharacterDetailsTest()
                coEvery { getCharacterFromDb(FAKE_CHARACTER_ID) } returns createCharacterDetailsTest()
            }

            val episodesInteractor = mockk<EpisodesInteractor> {
                coEvery { getEpisodeListByIdFromDb(FAKE_EPISODES_ID) } returns createEpisodesListTest()
            }

            CharacterDetailsViewModel(FAKE_CHARACTER_ID, episodesInteractor, charactersInteractor, mockk())
            coVerify {
                charactersInteractor.getCharacterFromDb(FAKE_CHARACTER_ID) wasNot Called
                episodesInteractor.getEpisodeListByIdFromDb(FAKE_EPISODES_ID) wasNot Called

            }
        }
    }

    private fun createCharacterDetailsTest() =
        Character(FAKE_CHARACTER_ID, "Rick", "", "", "",
            "", "", "", 1L, "", 1L, FAKE_EPISODES_ID)

    private fun createEpisodesListTest() = listOf(Episode(1, "Pilot", "", "", ""))

    companion object {

        private const val FAKE_CHARACTER_ID = 1L
        private const val FAKE_EPISODES_ID = "1"
    }

    class NetworkException() : Exception()


}
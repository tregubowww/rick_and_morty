package ru.tregubowww.rick_and_morty.character.character_details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.tregubowww.rick_and_morty.R
import ru.tregubowww.rick_and_morty.app.appComponent
import ru.tregubowww.rick_and_morty.databinding.FragmentCharacterDetailsBinding
import ru.tregubowww.rick_and_morty.state.State
import ru.tregubowww.rick_and_morty.utils.toastShow
import javax.inject.Inject
import kotlin.properties.ReadOnlyProperty

class CharacterDetailsFragment : Fragment(R.layout.fragment_character_details) {


    private val characterId: Long by ReadOnlyProperty { thisRef, _ ->
        requireNotNull(thisRef.requireArguments().getLong(CHARACTER_KEY))
    }

    private val viewBinding by viewBinding(FragmentCharacterDetailsBinding::bind)

    @Inject
    lateinit var viewModelFactory: CharacterDetailsViewModelFactory.Factory
    private val viewModel: CharacterDetailsViewModel by viewModels {
        viewModelFactory.create(characterId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireContext().appComponent.inject(this)
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.refreshCharacterDetails()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        loadingStateObserver()
        characterObserver()
        episodeListObserver()
        swipeRefreshListener()
    }

    private fun initRecyclerView() {

        val recyclerView = viewBinding.characterDetailsEpisodesRecyclerView
        recyclerView.adapter = viewModel.characterDetailsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadingStateObserver() {
        lifecycleScope.launch {
            viewModel.mutableState.collectLatest{
                when (it) {
                    is State.Error -> {
                        toastShow(getString(R.string.error_loading), requireContext())
                        viewBinding.progressBar.isVisible = false
                    }
                    is State.Loading -> viewBinding.progressBar.isVisible = true
                    is State.NotLoading -> viewBinding.progressBar.isVisible = false
                }
            }
        }
    }


    private fun characterObserver() {
        lifecycleScope.launch {
            viewModel.mutableCharacterDetails.collectLatest {
                viewModel.characterDetailsAdapter.character = it
            }
        }
    }


    private fun episodeListObserver() {
        lifecycleScope.launch {
            viewModel.mutableEpisodeList.collectLatest {
                viewModel.characterDetailsAdapter.episodeList = it
            }
        }
    }

    private fun swipeRefreshListener() {
        with(viewBinding.swipeRefreshLayout) {
            setOnRefreshListener {
                viewModel.refreshCharacterDetails()
                isRefreshing = false
            }
        }
    }

    companion object {
        fun newInstance(characterId: Long) = CharacterDetailsFragment().apply {
            arguments = Bundle().apply { putLong (CHARACTER_KEY, characterId) }
        }

        private const val CHARACTER_KEY = "CHARACTER_KEY"
    }
}
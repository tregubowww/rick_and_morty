package ru.tregubowww.rick_and_morty.episode.episode_details

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
import ru.tregubowww.rick_and_morty.databinding.FragmentEpisodeDetailsBinding
import ru.tregubowww.rick_and_morty.state.State
import ru.tregubowww.rick_and_morty.utils.changeSpanCount
import ru.tregubowww.rick_and_morty.utils.changeSpanCountForDetails
import ru.tregubowww.rick_and_morty.utils.toastShow
import javax.inject.Inject
import kotlin.properties.ReadOnlyProperty

class EpisodeDetailsFragment : Fragment(R.layout.fragment_episode_details) {


    private val episodeId: Long by ReadOnlyProperty { thisRef, _ ->
        requireNotNull(thisRef.requireArguments().getLong(EPISODE_KEY))
    }

    private val viewBinding by viewBinding(FragmentEpisodeDetailsBinding::bind)

    @Inject
    lateinit var viewModelFactory: EpisodeDetailsViewModelFactory.Factory
    private val viewModel: EpisodeDetailsViewModel by viewModels {
        viewModelFactory.create(episodeId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireContext().appComponent.inject(this)
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.refreshEpisodeDetails()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        loadingStateObserver()
        episodeObserver()
        episodeListObserver()
        swipeRefreshListener()
    }

    private fun initRecyclerView() {

        val recyclerView = viewBinding.characterRecyclerView
        recyclerView.adapter = viewModel.episodeDetailsAdapter
        changeSpanCountForDetails(recyclerView,resources, requireContext())
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


    private fun episodeObserver() {
        lifecycleScope.launch {
            viewModel.mutableEpisodeDetails.collectLatest {
                viewModel.episodeDetailsAdapter.episode = it
            }
        }
    }


    private fun episodeListObserver() {
        lifecycleScope.launch {
            viewModel.mutableCharacterList.collectLatest {
                viewModel.episodeDetailsAdapter.characterList = it
            }
        }
    }

    private fun swipeRefreshListener() {
        with(viewBinding.swipeRefreshLayout) {
            setOnRefreshListener {
                viewModel.refreshEpisodeDetails()
                isRefreshing = false
            }
        }
    }

    companion object {
        fun newInstance(episodeId: Long) = EpisodeDetailsFragment().apply {
            arguments = Bundle().apply { putLong (EPISODE_KEY, episodeId) }
        }

        private const val EPISODE_KEY = "EPISODE_KEY"
    }
}
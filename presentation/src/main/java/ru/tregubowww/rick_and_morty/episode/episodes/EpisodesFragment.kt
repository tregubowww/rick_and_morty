package ru.tregubowww.rick_and_morty.episode.episodes

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.addRepeatingJob
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.tregubowww.rick_and_morty.R
import ru.tregubowww.rick_and_morty.app.appComponent
import ru.tregubowww.rick_and_morty.databinding.FragmentEpisodesBinding
import ru.tregubowww.rick_and_morty.state.State
import ru.tregubowww.rick_and_morty.navigation.Navigation
import ru.tregubowww.rick_and_morty.utils.changeSpanCount
import ru.tregubowww.rick_and_morty.utils.toastShow
import javax.inject.Inject

class EpisodesFragment : Fragment(R.layout.fragment_episodes) {


    private val viewBinding by viewBinding(FragmentEpisodesBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: EpisodesViewModel by viewModels {
        viewModelFactory
    }

    @Inject
    lateinit var navigation: Navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        requireContext().appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        episodeListObserver()
        swipeRefreshListener()
        buttonClickListener()
        loadingStateObserver()
    }

    private fun initRecyclerView() {
        val recyclerView = viewBinding.recyclerViewEpisodes
        recyclerView.adapter = viewModel.episodesAdapter.withLoadStateFooter(viewModel.loaderStateAdapter)
        changeSpanCount(recyclerView, resources, requireContext())
    }



    private fun episodeListObserver() {
        addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.episodesFlow.collectLatest(viewModel.episodesAdapter::submitData)
        }

        viewModel.mutableEpisodesFromDb
            .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach{viewModel.episodesAdapter.submitData(PagingData.from(it))}
            .launchIn(lifecycleScope)
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

    private fun swipeRefreshListener() {
        with(viewBinding.swipeRefreshLayout) {
            setOnRefreshListener {
                viewModel.refreshEpisodes()
                isRefreshing = false
            }
        }
    }


    private fun buttonClickListener() {
        with(viewBinding){
            searchView.setOnClickListener {
                navigation.searchEpisodes()
            }

            filterImageView.setOnClickListener {
                navigation.episodesFilterChoice()
            }
        }
    }

    companion object {
        fun newInstance() = EpisodesFragment()
    }
}
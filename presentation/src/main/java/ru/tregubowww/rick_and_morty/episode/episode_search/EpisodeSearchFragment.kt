package ru.tregubowww.rick_and_morty.episode.episode_search

    import android.content.Context
    import android.os.Bundle
    import android.view.View
    import android.view.inputmethod.InputMethodManager
    import androidx.core.view.isVisible
    import androidx.core.widget.doAfterTextChanged
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
    import ru.tregubowww.rick_and_morty.databinding.FragmentEpisodeSearchBinding
    import ru.tregubowww.rick_and_morty.databinding.FragmentLocationSearchBinding
    import ru.tregubowww.rick_and_morty.state.State
    import ru.tregubowww.rick_and_morty.navigation.Navigation
    import ru.tregubowww.rick_and_morty.utils.changeSpanCount
    import ru.tregubowww.rick_and_morty.utils.toastShow
    import javax.inject.Inject

    class EpisodeSearchFragment : Fragment(R.layout.fragment_episode_search) {

        private val viewBinding by viewBinding(FragmentEpisodeSearchBinding::bind)

        @Inject
        lateinit var navigation: Navigation

        @Inject
        lateinit var viewModelFactory: ViewModelProvider.Factory
        private val viewModel: EpisodeSearchViewModel by viewModels {
            viewModelFactory
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            requireContext().appComponent.inject(this)
            super.onCreate(savedInstanceState)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            initRecyclerView()
            episodeSearchObserver()
            editTextChangeListener()
            swipeRefreshListener()
            loadingStateObserver()
            buttonClickListener()
        }

        private fun initRecyclerView() {
            val recyclerView = viewBinding.recyclerViewEpisodes
            recyclerView.adapter = viewModel.episodesAdapter.withLoadStateFooter(viewModel.loaderStateAdapter)
            changeSpanCount(recyclerView, resources, requireContext())
        }

        private fun episodeSearchObserver() {

            addRepeatingJob(Lifecycle.State.STARTED) {
                viewModel.episodesFlow.collectLatest(viewModel.episodesAdapter::submitData)
            }

            viewModel.mutableSearchEpisodesFromDb
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .onEach { viewModel.episodesAdapter.submitData(PagingData.from(it)) }
                .launchIn(lifecycleScope)
        }

        private fun swipeRefreshListener() {
            with(viewBinding.swipeRefreshLayout) {
                setOnRefreshListener {
                    viewModel.refresh()
                    isRefreshing = false
                }
            }
        }

        private fun editTextChangeListener() {

            with(viewBinding.searchEditText) {

                requestFocus()
                val inputMethodManager: InputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                inputMethodManager.showSoftInput(viewBinding.searchEditText, InputMethodManager.SHOW_IMPLICIT)

                doAfterTextChanged { text ->
                    viewModel.setQuery(text?.toString() ?: "")
                }
            }


            viewModel.querySearch
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .onEach(::updateSearchQuery)
                .launchIn(lifecycleScope)
        }

        private fun loadingStateObserver() {
            lifecycleScope.launch {
                viewModel.mutableState.collectLatest {
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

        private fun buttonClickListener() {
            with(viewBinding) {
                buttonBackImageView.setOnClickListener {
                    navigation.back()
                }

                clearButtonImageView.setOnClickListener {
                    searchEditText.setText("")
                }
            }
        }

        private fun updateSearchQuery(searchQuery: String) {
            with(viewBinding.searchEditText) {
                if ((text?.toString() ?: "") != searchQuery) {
                    setText(searchQuery)
                }
            }
        }

        companion object {
            fun newInstance() = EpisodeSearchFragment()
        }
    }

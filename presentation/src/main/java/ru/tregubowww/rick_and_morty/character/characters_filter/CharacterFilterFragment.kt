package ru.tregubowww.rick_and_morty.character.characters_filter

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.tregubowww.domain.query.FilterQueryCharacter
import ru.tregubowww.rick_and_morty.R
import ru.tregubowww.rick_and_morty.app.appComponent
import ru.tregubowww.rick_and_morty.databinding.FragmentCharactersFilterBinding
import ru.tregubowww.rick_and_morty.state.State
import ru.tregubowww.rick_and_morty.navigation.Navigation
import ru.tregubowww.rick_and_morty.utils.changeSpanCount
import ru.tregubowww.rick_and_morty.utils.toastShow
import javax.inject.Inject
import kotlin.properties.ReadOnlyProperty

class CharacterFilterFragment : Fragment(R.layout.fragment_characters_filter) {


    private val filterQueryCharacter: FilterQueryCharacter by ReadOnlyProperty { thisRef, _ ->
        requireNotNull(thisRef.requireArguments().getParcelable(FILTER_CHARACTER_KEY))
    }

    private val viewBinding by viewBinding(FragmentCharactersFilterBinding::bind)

    @Inject
    lateinit var viewModelFactory: CharactersFilterViewModelFactory.Factory
    private val viewModel: CharactersFilterViewModel by viewModels {
        viewModelFactory.create(filterQueryCharacter)
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
        characterListObserver()
        swipeRefreshListener()
        buttonClickListener()
        loadingStateObserver()
    }

    private fun initRecyclerView() {
        val recyclerView = viewBinding.recyclerViewCharacters
        recyclerView.adapter = viewModel.charactersAdapter.withLoadStateFooter(viewModel.loaderStateAdapter)
        changeSpanCount(recyclerView, resources, requireContext())
    }

    private fun characterListObserver() {
        addRepeatingJob(Lifecycle.State.STARTED) {

            viewModel.charactersFlow.collect(viewModel.charactersAdapter::submitData)
        }

        viewModel.mutableFilterCharactersFromDb
            .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { viewModel.charactersAdapter.submitData(PagingData.from(it)) }
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

    private fun swipeRefreshListener() {
        with(viewBinding.swipeRefreshLayout) {
            setOnRefreshListener {
                viewModel.refreshCharacters()
                isRefreshing = false
            }
        }
    }

    private fun buttonClickListener() {
        viewBinding.buttonBackImageView.setOnClickListener {
            navigation.back()
        }
    }

    companion object {
        fun newInstance(filterQueryCharacter: FilterQueryCharacter) = CharacterFilterFragment().apply {
            arguments = Bundle().apply { putParcelable(FILTER_CHARACTER_KEY, filterQueryCharacter) }
        }

        private const val FILTER_CHARACTER_KEY = "FILTER_CHARACTER_KEY"
    }
}

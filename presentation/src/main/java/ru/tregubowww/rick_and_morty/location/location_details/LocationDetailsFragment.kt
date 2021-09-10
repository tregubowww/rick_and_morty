package ru.tregubowww.rick_and_morty.location.location_details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.tregubowww.rick_and_morty.R
import ru.tregubowww.rick_and_morty.app.appComponent
import ru.tregubowww.rick_and_morty.databinding.FragmentLocationDetailsBinding
import ru.tregubowww.rick_and_morty.state.State
import ru.tregubowww.rick_and_morty.utils.changeSpanCount
import ru.tregubowww.rick_and_morty.utils.changeSpanCountForDetails
import ru.tregubowww.rick_and_morty.utils.toastShow
import javax.inject.Inject
import kotlin.properties.ReadOnlyProperty

class LocationDetailsFragment : Fragment(R.layout.fragment_location_details) {


    private val locationId: Long by ReadOnlyProperty { thisRef, _ ->
        requireNotNull(thisRef.requireArguments().getLong(LOCATION_KEY))
    }

    private val viewBinding by viewBinding(FragmentLocationDetailsBinding::bind)

    @Inject
    lateinit var viewModelFactory: LocationDetailsViewModelFactory.Factory
    private val viewModel: LocationDetailsViewModel by viewModels {
        viewModelFactory.create(locationId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireContext().appComponent.inject(this)
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.refreshLocationDetails()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        loadingStateObserver()
        locationObserver()
        episodeListObserver()
        swipeRefreshListener()
    }

    private fun initRecyclerView() {

        val recyclerView = viewBinding.characterRecyclerView
        recyclerView.adapter = viewModel.locationDetailsAdapter
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


    private fun locationObserver() {
        lifecycleScope.launch {
            viewModel.mutableLocationDetails.collectLatest {
                viewModel.locationDetailsAdapter.location = it
            }
        }
    }


    private fun episodeListObserver() {
        lifecycleScope.launch {
            viewModel.mutableCharacterList.collectLatest {
                viewModel.locationDetailsAdapter.characterList = it
            }
        }
    }

    private fun swipeRefreshListener() {
        with(viewBinding.swipeRefreshLayout) {
            setOnRefreshListener {
                viewModel.refreshLocationDetails()
                isRefreshing = false
            }
        }
    }

    companion object {
        fun newInstance(locationId: Long) = LocationDetailsFragment().apply {
            arguments = Bundle().apply { putLong (LOCATION_KEY, locationId) }
        }

        private const val LOCATION_KEY = "LOCATION_KEY"
    }
}
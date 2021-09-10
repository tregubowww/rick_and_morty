package ru.tregubowww.rick_and_morty.episode.episodes_filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.tregubowww.domain.query.FilterQueryEpisode
import ru.tregubowww.rick_and_morty.R
import ru.tregubowww.rick_and_morty.app.appComponent
import ru.tregubowww.rick_and_morty.databinding.FragmentEpisodeFilterChoiceBinding
import ru.tregubowww.rick_and_morty.navigation.Navigation
import javax.inject.Inject

class EpisodeFilterChoiceFragment : Fragment(R.layout.fragment_episode_filter_choice) {

    private val viewBinding by viewBinding(FragmentEpisodeFilterChoiceBinding::bind)

    @Inject
    lateinit var navigation: Navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

        with(viewBinding) {
            buttonSearch.setOnClickListener {

                val nameText = getTextFromEditText(nameEditText.text.toString())
                val episodeText = getTextFromEditText(episodeEditText.text.toString())

                val filterQuery = FilterQueryEpisode(
                    name = nameText,
                    episode = episodeText,
                )
                navigation.episodesFilter(filterQuery)
            }
            buttonCancel.setOnClickListener { navigation.back() }
        }
    }

    private fun getTextFromEditText(text: String): String? {
        return if (text.isBlank()) null
        else text
    }

    companion object {
        fun newInstance() = EpisodeFilterChoiceFragment()
    }
}
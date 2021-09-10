package ru.tregubowww.rick_and_morty.character.character_details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.tregubowww.rick_and_morty.navigation.Navigation
import javax.inject.Inject
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.rick_and_morty.character.CharactersViewHolder
import ru.tregubowww.rick_and_morty.databinding.ViewHolderCharactersBinding
import ru.tregubowww.rick_and_morty.databinding.ViewHolderHeaderEpisodeDetailsBinding
import ru.tregubowww.rick_and_morty.location.location_details.CharacterDiffUtils

class EpisodeDetailsAdapter @Inject constructor(private val navigation: Navigation, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var characterList = listOf<Character>()
        set(value) {
            val contactDiffUtils = CharacterDiffUtils(field, value)
            val contactDiffResult = DiffUtil.calculateDiff(contactDiffUtils)
            field = value
            contactDiffResult.dispatchUpdatesTo(this)
        }
    var episode: Episode? = null
        set(value) {
            field = value
            notifyItemChanged(TYPE_HEADER)
        }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            else -> TYPE_CHARACTER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(ViewHolderHeaderEpisodeDetailsBinding.inflate(layoutInflater, parent, false))
            else -> CharactersViewHolder(ViewHolderCharactersBinding.inflate(layoutInflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> episode?.let { holder.bind(it, navigation) }
            is CharactersViewHolder -> if (characterList.isNotEmpty()) holder.bind(characterList[position - HEADERS_COUNT], navigation, context)
        }
    }

    override fun getItemCount() = if (characterList.isEmpty()) 0 else characterList.size + HEADERS_COUNT

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_CHARACTER = 1
        private const val HEADERS_COUNT = 1
    }

    class HeaderViewHolder(
        private val binding: ViewHolderHeaderEpisodeDetailsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(episode: Episode, navigation: Navigation) {
            with(binding) {
                nameTextView.text = episode.name
                episodeTextView.text = episode.episode
                valueReleaseDateTextView.text = episode.airDate
                backgroundForBackImageView.setOnClickListener { navigation.back() }
            }
        }
    }
}

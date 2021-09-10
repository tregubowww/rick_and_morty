package ru.tregubowww.rick_and_morty.location.location_details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.tregubowww.rick_and_morty.navigation.Navigation
import javax.inject.Inject
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.domain.entity.Location
import ru.tregubowww.rick_and_morty.character.CharactersViewHolder
import ru.tregubowww.rick_and_morty.character.character_details.EpisodeDetailsAdapter
import ru.tregubowww.rick_and_morty.databinding.ViewHolderCharactersBinding
import ru.tregubowww.rick_and_morty.databinding.ViewHolderHeaderLocationDetailsBinding

class LocationDetailsAdapter @Inject constructor(private val navigation: Navigation , private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var characterList = listOf<Character>()
        set(value) {
            val contactDiffUtils = CharacterDiffUtils(field, value)
            val contactDiffResult = DiffUtil.calculateDiff(contactDiffUtils)
            field = value
            contactDiffResult.dispatchUpdatesTo(this)
        }
    var location: Location? = null
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
            TYPE_HEADER -> HeaderViewHolder(ViewHolderHeaderLocationDetailsBinding.inflate(layoutInflater, parent, false))
            else -> CharactersViewHolder(ViewHolderCharactersBinding.inflate(layoutInflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> location?.let { holder.bind(it, navigation) }
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
        private val binding: ViewHolderHeaderLocationDetailsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: Location, navigation: Navigation) {
            with(binding) {
                nameTextView.text = location.name
                typeTextView.text = location.type
                dimensionTextView.text = location.dimension
                backgroundForBackImageView.setOnClickListener { navigation.back() }
            }
        }
    }
}

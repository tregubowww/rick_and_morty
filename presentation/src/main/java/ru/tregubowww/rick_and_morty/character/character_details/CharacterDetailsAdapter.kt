package ru.tregubowww.rick_and_morty.character.character_details

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.rick_and_morty.databinding.ViewHolderEpisodeForCharacterDetailsBinding
import ru.tregubowww.rick_and_morty.databinding.ViewHolderHeaderCharacterDetailsBinding
import ru.tregubowww.rick_and_morty.databinding.ViewHolderLocationForCharacterDetailsBinding
import ru.tregubowww.rick_and_morty.navigation.Navigation
import javax.inject.Inject
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.rick_and_morty.R

class CharacterDetailsAdapter @Inject constructor(private val navigation: Navigation, private val context: Context ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var episodeList = emptyList<Episode>()
        set(value) {
            val contactDiffUtils = EpisodesDiffUtils(field, value)
            val contactDiffResult = DiffUtil.calculateDiff (contactDiffUtils, false)
            field = value
            contactDiffResult.dispatchUpdatesTo(this)
        }
    var character: Character? = null
        set(value) {
            field = value
            notifyItemChanged(TYPE_HEADER)
            notifyItemChanged(TYPE_LOCATION)
            notifyItemChanged(TYPE_ORIGIN_LOCATION)
        }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            1 -> TYPE_LOCATION
            2 -> TYPE_ORIGIN_LOCATION
            else -> TYPE_EPISODE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(ViewHolderHeaderCharacterDetailsBinding.inflate(layoutInflater, parent, false))
            TYPE_LOCATION -> LocationViewHolder(ViewHolderLocationForCharacterDetailsBinding.inflate(layoutInflater, parent, false))
            TYPE_ORIGIN_LOCATION -> OriginLocationViewHolder(ViewHolderLocationForCharacterDetailsBinding.inflate(layoutInflater, parent, false))
            else -> EpisodeViewHolder(ViewHolderEpisodeForCharacterDetailsBinding.inflate(layoutInflater, parent, false))
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> character?.let { holder.bind(it, navigation, context) }
            is LocationViewHolder -> character?.let { holder.bind(it, navigation, context) }
            is OriginLocationViewHolder -> character?.let { holder.bind(it, navigation, context) }
            is EpisodeViewHolder -> if (episodeList.isNotEmpty()) holder.bind(episodeList[position - HEADERS_COUNT], navigation)
        }
    }

    override fun getItemCount() = if (episodeList.isEmpty()) 0 else episodeList.size + HEADERS_COUNT

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_LOCATION = 1
        private const val TYPE_ORIGIN_LOCATION = 2
        private const val TYPE_EPISODE = 3
        private const val HEADERS_COUNT = 3
    }

    class HeaderViewHolder(
        private val binding: ViewHolderHeaderCharacterDetailsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character, navigation: Navigation, context: Context) {
            with(binding) {
                Glide
                    .with(root)
                    .load(character.image)
                    .circleCrop()
                    .into(characterImageView)
                nameTextView.text = character.name
                valueGenderTextView.text = character.gender
                valueRaceTextView.text = character.species
                statusTextView.text = character.status
                when (character.status) {
                    "Alive" -> statusTextView.setTextColor(ContextCompat.getColor(context, R.color.green))
                    "Dead" -> statusTextView.setTextColor(ContextCompat.getColor(context, R.color.ping))
                    "unknown" -> statusTextView.setTextColor(ContextCompat.getColor(context, R.color.gray))
                }
                backgroundForBackImageView.setOnClickListener { navigation.back() }
            }
        }
    }

    class EpisodeViewHolder(
        private val binding: ViewHolderEpisodeForCharacterDetailsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemEpisode: Episode, navigate: Navigation) {
            with(binding) {
                numberEpisodeTextView.text = itemEpisode.episode
                nameEpisodeTextView.text = itemEpisode.name
                dateTextView.text = itemEpisode.airDate
                root.setOnClickListener { navigate.episodeDetails(itemEpisode.id) }
            }
        }
    }

    class LocationViewHolder(
        private val binding: ViewHolderLocationForCharacterDetailsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character, navigation: Navigation, context: Context) {
            with(binding) {
                nextLocationImageView.isVisible = character.originIdLocation != null
                locationView.isVisible = character.originIdLocation != null
                valueLocationTextView.text =
                    if (character.nameLocation.isNotEmpty()) character.nameLocation
                    else context.getString(R.string.unknown)
                root.setOnClickListener { character.originIdLocation?.let { id -> navigation.locationDetails(id) } }
            }
        }
    }

    class OriginLocationViewHolder(
        private val binding: ViewHolderLocationForCharacterDetailsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character, navigation: Navigation,  context: Context) {
            with(binding) {
                nextLocationImageView.isVisible = character.originIdLocation != null
                locationView.isVisible = character.originIdLocation != null
                locationTextView.text = context.getString(R.string.origin_location)
                binding.valueLocationTextView.text =
                    if (character.originNameLocation.isNotEmpty()) character.originNameLocation
                    else context.getString(R.string.unknown)

                root.setOnClickListener { character.originIdLocation?.let { id -> navigation.locationDetails(id) } }
            }
        }
    }
}

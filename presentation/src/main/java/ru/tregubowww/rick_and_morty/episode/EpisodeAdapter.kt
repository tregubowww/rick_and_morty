package ru.tregubowww.rick_and_morty.episode

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.domain.entity.Location
import ru.tregubowww.rick_and_morty.databinding.ViewHolderEpisodeBinding
import ru.tregubowww.rick_and_morty.databinding.ViewHolderLocationsBinding
import ru.tregubowww.rick_and_morty.navigation.Navigation
import javax.inject.Inject

class EpisodeAdapter @Inject constructor(private val navigation: Navigation, private val context: Context) :
    PagingDataAdapter<Episode, EpisodeAdapter.EpisodeViewHolder>(EPISODE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderEpisodeBinding.inflate(layoutInflater, parent, false)
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(itemEpisode = getItem(position), navigation = navigation)
    }

    companion object {
        private val EPISODE_COMPARATOR = object : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    class EpisodeViewHolder(private val binding: ViewHolderEpisodeBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(itemEpisode: Episode?, navigation: Navigation,) {
            with(binding) {
                nameTextView.text = itemEpisode?.name
                episodeTextView.text = itemEpisode?.episode
                dateTextView.text = itemEpisode?.airDate
                root.setOnClickListener {
                    itemEpisode?.let { episode ->
                        navigation.episodeDetails(id = episode.id)
                    }
                }
                }
            }
        }
    }


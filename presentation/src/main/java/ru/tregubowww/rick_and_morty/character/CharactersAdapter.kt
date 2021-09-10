package ru.tregubowww.rick_and_morty.character

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.rick_and_morty.databinding.ViewHolderCharactersBinding
import ru.tregubowww.rick_and_morty.navigation.Navigation
import javax.inject.Inject

class CharactersAdapter @Inject constructor(private val navigation: Navigation, private val context: Context) :
    PagingDataAdapter<Character, CharactersViewHolder>(CHARACTER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderCharactersBinding.inflate(layoutInflater, parent, false)
        return CharactersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(itemCharacter = getItem(position), navigation = navigation, context = context)
    }

    companion object {
        private val CHARACTER_COMPARATOR = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}



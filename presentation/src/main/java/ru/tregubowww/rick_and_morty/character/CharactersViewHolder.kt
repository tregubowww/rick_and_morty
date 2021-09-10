package ru.tregubowww.rick_and_morty.character

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.rick_and_morty.R
import ru.tregubowww.rick_and_morty.databinding.ViewHolderCharactersBinding
import ru.tregubowww.rick_and_morty.navigation.Navigation

class CharactersViewHolder(private val binding: ViewHolderCharactersBinding) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(itemCharacter: Character?, navigation: Navigation, context: Context) {
        with(binding) {
            nameTextView.text = itemCharacter?.name
            speciesAndGenderTextView.text = "${itemCharacter?.species}, ${itemCharacter?.gender}"
            setStatusText(statusTextView, itemCharacter?.status, context)
            loadCharacterImage(root, itemCharacter?.image, characterImageView)
            root.setOnClickListener {
                itemCharacter?.let { character ->
                    navigation.characterDetails(id = character.id)
                }
            }
        }
    }

    private fun loadCharacterImage(root: LinearLayoutCompat, posterPath: String?, poster: ImageView) {
        Glide
            .with(root)
            .load(posterPath)
            .circleCrop()
            .into(poster)
    }

    private fun setStatusText(statusTextView: TextView, status: String?, context: Context) {
        statusTextView.text = status
        when (status) {
            "Alive" -> statusTextView.setTextColor(ContextCompat.getColor(context, R.color.green))
            "Dead" -> statusTextView.setTextColor(ContextCompat.getColor(context, R.color.ping))
            "unknown" -> statusTextView.setTextColor(ContextCompat.getColor(context, R.color.gray))
        }
    }
}
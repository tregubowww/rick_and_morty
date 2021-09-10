package ru.tregubowww.rick_and_morty.character.character_details

import androidx.recyclerview.widget.DiffUtil
import ru.tregubowww.domain.entity.Episode

class EpisodesDiffUtils(
    private val oldList: List<Episode>,
    private val newList: List<Episode>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

}
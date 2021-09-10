package ru.tregubowww.rick_and_morty.location.location_details

import androidx.recyclerview.widget.DiffUtil
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.domain.entity.Episode

class CharacterDiffUtils(
    private val oldList: List<Character>,
    private val newList: List<Character>,
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
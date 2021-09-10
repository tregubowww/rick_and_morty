package ru.tregubowww.domain.query

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterQueryCharacter(
    val name: String?,
    val status: String?,
    val species: String?,
    val gender: String?,
    val type: String?,
) : Parcelable

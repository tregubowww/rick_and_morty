package ru.tregubowww.domain.query

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterQueryEpisode(
    val name: String?,
    val episode: String?,
) : Parcelable

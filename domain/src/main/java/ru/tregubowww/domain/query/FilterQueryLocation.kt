package ru.tregubowww.domain.query

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterQueryLocation(
    val name: String?,
    val dimension: String?,
    val type: String?,
) : Parcelable

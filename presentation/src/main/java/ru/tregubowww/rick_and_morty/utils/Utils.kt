package ru.tregubowww.rick_and_morty.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.Gravity
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun changeSpanCount(recyclerView: RecyclerView, resources: Resources, context: Context) {
    val spanCountPortrait = 2
    val spanCountLandscape = 3

    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        recyclerView.layoutManager =
            GridLayoutManager(context, spanCountPortrait)
    } else {
        recyclerView.layoutManager =
            GridLayoutManager(context, spanCountLandscape)
    }
}

fun changeSpanCountForDetails(recyclerView: RecyclerView, resources: Resources, context: Context) {

    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    0 -> 2
                    else -> 1
                }
            }
        }
        recyclerView.layoutManager = layoutManager
    }else {
        val layoutManager = GridLayoutManager(context, 6)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    0, -> 6
                    else -> 2
                }
            }
        }
        recyclerView.layoutManager = layoutManager
    }
}

fun toastShow(text: String?, context: Context) {
    val toast = Toast.makeText(
        context,
        text,
        Toast.LENGTH_SHORT
    )
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

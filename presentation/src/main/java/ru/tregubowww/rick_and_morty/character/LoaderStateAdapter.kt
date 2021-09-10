package ru.tregubowww.rick_and_morty.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.tregubowww.rick_and_morty.databinding.ViewHolderLoaderStateBinding
import javax.inject.Inject

class LoaderStateAdapter @Inject constructor() :
    LoadStateAdapter<LoaderStateAdapter.LoaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderLoaderStateBinding.inflate(layoutInflater, parent, false)
        return LoaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoaderViewHolder(val binding: ViewHolderLoaderStateBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(loadState: LoadState) {
            binding.pbLoader.isVisible = loadState is LoadState.Loading
        }
    }
}

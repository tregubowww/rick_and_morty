package ru.tregubowww.rick_and_morty.location

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.tregubowww.domain.entity.Location
import ru.tregubowww.rick_and_morty.databinding.ViewHolderLocationsBinding
import ru.tregubowww.rick_and_morty.navigation.Navigation
import javax.inject.Inject

class LocationsAdapter @Inject constructor(private val navigation: Navigation, private val context: Context) :
    PagingDataAdapter<Location, LocationsAdapter.LocationsViewHolder>(LOCATION_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderLocationsBinding.inflate(layoutInflater, parent, false)
        return LocationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        holder.bind(itemLocation = getItem(position), navigation = navigation)
    }

    companion object {
        private val LOCATION_COMPARATOR = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    class LocationsViewHolder(private val binding: ViewHolderLocationsBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(itemLocation: Location?, navigation: Navigation) {
            with(binding) {
                nameTextView.text = itemLocation?.name
                typeAndDimensionTextView.text = "${itemLocation?.type} - ${itemLocation?.dimension}"
                root.setOnClickListener {
                    itemLocation?.let { location ->
                        navigation.locationDetails(id = location.id)
                    }
                }
                }
            }
        }
    }


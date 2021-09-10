package ru.tregubowww.domain.repository

import androidx.paging.PagingSource
import ru.tregubowww.domain.entity.Location
import ru.tregubowww.domain.query.FilterQueryLocation

interface LocationRepository {
    fun getAllLocations(): PagingSource<Int, Location>
    fun searchLocation(querySearch: String): PagingSource<Int, Location>
    suspend fun searchLocationFromDb(querySearch: String): List<Location>

    suspend fun getLocationFromNetwork(locationId: Long): Location
    suspend fun getLocationFromDb(locationId: Long): Location
    suspend fun getLocationListFromDb(): List<Location>
    fun filterLocations(filterQueryLocation: FilterQueryLocation): PagingSource<Int, Location>
    suspend fun filterLocationsFromDb(filterQueryLocation: FilterQueryLocation): List<Location>
}
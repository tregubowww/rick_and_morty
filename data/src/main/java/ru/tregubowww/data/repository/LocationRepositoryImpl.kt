package ru.tregubowww.data.repository

import androidx.paging.PagingSource
import ru.tregubowww.data.db.dao.LocationDao
import ru.tregubowww.data.network.service.LocationService
import ru.tregubowww.data.paging_source.locations.FilterLocationPagingSource
import ru.tregubowww.data.paging_source.locations.LocationPagingSource
import ru.tregubowww.data.paging_source.locations.SearchLocationPagingSource
import ru.tregubowww.data.toLocation
import ru.tregubowww.domain.entity.Location
import ru.tregubowww.domain.query.FilterQueryLocation
import ru.tregubowww.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationService: LocationService,
    private val locationDao: LocationDao,
    private val searchLocationPagingSource: SearchLocationPagingSource.Factory,
    private val filterLocationPagingSource: FilterLocationPagingSource.Factory,
) : LocationRepository {

    override fun getAllLocations(): PagingSource<Int, Location> =
        LocationPagingSource(locationService, locationDao)

    override fun searchLocation(querySearch: String): PagingSource<Int, Location> =
        searchLocationPagingSource.create(querySearch)

    override suspend fun searchLocationFromDb(querySearch: String): List<Location> =
        locationDao.searchLocation("%${querySearch}%")

    override suspend fun getLocationFromNetwork(locationId: Long): Location =
        locationService.getLocation(locationId).toLocation()

    override suspend fun getLocationFromDb(locationId: Long): Location =
        locationDao.getLocation(locationId)

    override suspend fun getLocationListFromDb(): List<Location> =
        locationDao.getListLocation()

    override fun filterLocations(filterQueryLocation: FilterQueryLocation): PagingSource<Int, Location> =
        filterLocationPagingSource.create(filterQueryLocation)

    override suspend fun filterLocationsFromDb(filterQueryLocation: FilterQueryLocation): List<Location> =
        locationDao.filterLocation(
            name = "%${filterQueryLocation.name ?: ""}%",
            type = filterQueryLocation.type ?: "%",
            dimension = filterQueryLocation.dimension ?: "%"
        )
}
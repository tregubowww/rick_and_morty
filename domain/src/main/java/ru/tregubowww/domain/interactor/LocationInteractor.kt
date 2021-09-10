package ru.tregubowww.domain.interactor

import androidx.paging.PagingSource
import ru.tregubowww.domain.entity.Location
import ru.tregubowww.domain.query.FilterQueryCharacter
import ru.tregubowww.domain.query.FilterQueryLocation
import ru.tregubowww.domain.repository.LocationRepository
import javax.inject.Inject

interface LocationInteractor {
    fun getAllLocations(): PagingSource<Int, Location>
    fun searchLocation(querySearch: String): PagingSource<Int, Location>
    suspend fun searchLocationFromDb(querySearch: String): List<Location>

    suspend fun getLocationFromNetwork(locationId: Long): Location
    suspend fun getLocationFromDb(locationId: Long): Location
    suspend fun getLocationListFromDb(): List<Location>
    fun filterLocations(queryFilterLocation: FilterQueryLocation): PagingSource<Int, Location>
    suspend fun filterLocationsFromDb(queryFilterLocation: FilterQueryLocation): List<Location>
}

class LocationInteractorImpl @Inject constructor(
    private val locationIdRepository: LocationRepository,
) : LocationInteractor {

    override fun getAllLocations(): PagingSource<Int, Location> =
        locationIdRepository.getAllLocations()

    override fun searchLocation(querySearch: String): PagingSource<Int, Location> =
        locationIdRepository.searchLocation(querySearch)

    override suspend fun searchLocationFromDb(querySearch: String) =
        locationIdRepository.searchLocationFromDb(querySearch)

    override suspend fun getLocationFromNetwork(locationId: Long): Location =
        locationIdRepository.getLocationFromNetwork(locationId)

    override suspend fun getLocationFromDb(locationId: Long): Location =
        locationIdRepository.getLocationFromDb(locationId)

    override suspend fun getLocationListFromDb(): List<Location> =
        locationIdRepository.getLocationListFromDb()

    override fun filterLocations(queryFilterLocation: FilterQueryLocation): PagingSource<Int, Location> =
        locationIdRepository.filterLocations(queryFilterLocation)

    override suspend fun filterLocationsFromDb(queryFilterLocation: FilterQueryLocation): List<Location> =
        locationIdRepository.filterLocationsFromDb(queryFilterLocation)
}

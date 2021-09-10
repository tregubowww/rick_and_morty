package ru.tregubowww.data.network.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.tregubowww.data.network.model.LocationDto
import ru.tregubowww.data.network.model.LocationDtoResult

interface LocationService {

    @GET("location/")
    suspend fun getLocationList(@Query("page") query: Int): LocationDto

    @GET("location/")
    suspend fun getLocationsFilter(
        @Query("name") name: String?,
        @Query("type") type: String?,
        @Query("dimension") dimension: String?,
        @Query("page") page: Int,
    ): LocationDto

    @GET("location/")
    suspend fun getLocationsSearch(
        @Query("name") name: String,
        @Query("page") page: Int,
        ): LocationDto

    @GET("location/{id}")
    suspend fun getLocation(
        @Path("id") id: Long,
    ): LocationDtoResult
}
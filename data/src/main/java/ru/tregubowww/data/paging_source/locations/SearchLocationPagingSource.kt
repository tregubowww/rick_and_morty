package ru.tregubowww.data.paging_source.locations

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import ru.tregubowww.data.db.dao.LocationDao
import ru.tregubowww.data.network.model.LocationDto
import ru.tregubowww.data.network.service.LocationService
import ru.tregubowww.data.toLocation
import ru.tregubowww.domain.entity.Location
import java.io.IOException

class SearchLocationPagingSource @AssistedInject constructor(
    private val locationService: LocationService,
    private val locationDao: LocationDao,
    @Assisted("querySearch") private val querySearch: String,
) : PagingSource<Int, Location>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Location> {
        val page: Int = params.key ?: STARTING_PAGE_INDEX
        try {
            if (querySearch.isBlank()) return Page(emptyList(), prevKey = null, nextKey = null)

            val apiResponse = locationService.getLocationsSearch(
                name = querySearch,
                page = page
            )

            val locationList = apiResponse.results.map { it.toLocation() }

            locationDao.insertAllLocations(locationList)

            val nextPageNumber = getNextPageNumber(apiResponse)
            val prevPageNumber = getPrevPageNumber(apiResponse)

            return Page(
                data = locationList,
                prevKey = prevPageNumber,
                nextKey = nextPageNumber,
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Location>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    private fun getNextPageNumber(apiResponse: LocationDto): Int? {
        return if (apiResponse.info.next != null) {
            val uriNext = Uri.parse(apiResponse.info.next)
            val nextPageQuery = uriNext.getQueryParameter(PAGE)
            return nextPageQuery?.toInt()
        } else null
    }

    private fun getPrevPageNumber(apiResponse: LocationDto): Int? {
        return if (apiResponse.info.prev != null) {
            val uriPrev = Uri.parse(apiResponse.info.prev)
            val prevPageQuery = uriPrev.getQueryParameter(PAGE)
            prevPageQuery?.toInt()
        } else null
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("querySearch") querySearch: String,
        ): SearchLocationPagingSource
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
        private const val PAGE = "page"
    }
}
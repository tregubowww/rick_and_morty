package ru.tregubowww.data.paging_source.episodes

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import ru.tregubowww.data.db.dao.EpisodeDao
import ru.tregubowww.data.network.model.EpisodeDto
import ru.tregubowww.data.network.service.EpisodeService
import ru.tregubowww.data.toEpisode
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.domain.query.FilterQueryEpisode
import java.io.IOException

class FilterEpisodePagingSource @AssistedInject constructor(
    private val episodeService: EpisodeService,
    private val episodeDao: EpisodeDao,
    @Assisted("queryFilter") private val filterQueryEpisode: FilterQueryEpisode,
) : PagingSource<Int, Episode>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Episode> {
        val page: Int = params.key ?: STARTING_PAGE_INDEX
        return try {
            val apiResponse = episodeService.getEpisodesFilter(
                name = filterQueryEpisode.name,
                episode = filterQueryEpisode.episode,
                page = page
            )
            val episodeList = apiResponse.results.map { it.toEpisode() }

            episodeDao.insertAllEpisodes(episodeList)

            val nextPageNumber = getNextPageNumber(apiResponse)
            val prevPageNumber = getPrevPageNumber(apiResponse)

            Page(
                episodeList,
                prevKey = prevPageNumber,
                nextKey = nextPageNumber
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Episode>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    private fun getNextPageNumber(apiResponse: EpisodeDto): Int? {
        return if (apiResponse.info.next != null) {
            val uriNext = Uri.parse(apiResponse.info.next)
            val nextPageQuery = uriNext.getQueryParameter(PAGE)
            return nextPageQuery?.toInt()
        } else null
    }

    private fun getPrevPageNumber(apiResponse: EpisodeDto): Int? {
        return if (apiResponse.info.prev != null) {
            val uriPrev = Uri.parse(apiResponse.info.prev)
            val prevPageQuery = uriPrev.getQueryParameter(PAGE)
            prevPageQuery?.toInt()
        } else null
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("queryFilter") filterQueryEpisode: FilterQueryEpisode,
        ): FilterEpisodePagingSource
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
        private const val PAGE = "page"
    }
}
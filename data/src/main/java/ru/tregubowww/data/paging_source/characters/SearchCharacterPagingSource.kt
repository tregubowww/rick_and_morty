package ru.tregubowww.data.paging_source.characters

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import ru.tregubowww.data.db.dao.CharacterDao
import ru.tregubowww.data.network.model.CharactersDto
import ru.tregubowww.data.network.service.CharacterService
import ru.tregubowww.data.toCharacter
import ru.tregubowww.domain.entity.Character
import java.io.IOException

class SearchCharacterPagingSource @AssistedInject constructor(
    private val characterService: CharacterService,
    private val characterDao: CharacterDao,
    @Assisted("querySearch") private val querySearch: String,
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val page: Int = params.key ?: STARTING_PAGE_INDEX
        try {
            if (querySearch.isBlank()) return Page(emptyList(), prevKey = null, nextKey = null)

            val apiResponse = characterService.getCharactersSearch(
                name = querySearch, page = page
            )

            val characterList = apiResponse.results.map { it.toCharacter() }

            characterDao.insertAllCharacters(characterList)

            val nextPageNumber = getNextPageNumber(apiResponse)
            val prevPageNumber = getPrevPageNumber(apiResponse)

            return Page(
                data = characterList,
                prevKey = prevPageNumber,
                nextKey = nextPageNumber,
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    private fun getNextPageNumber(apiResponse: CharactersDto): Int? {
        return if (apiResponse.info.next != null) {
            val uriNext = Uri.parse(apiResponse.info.next)
            val nextPageQuery = uriNext.getQueryParameter(PAGE)
            return nextPageQuery?.toInt()
        } else null
    }

    private fun getPrevPageNumber(apiResponse: CharactersDto): Int? {
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
        ): SearchCharacterPagingSource
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
        private const val PAGE = "page"
    }
}
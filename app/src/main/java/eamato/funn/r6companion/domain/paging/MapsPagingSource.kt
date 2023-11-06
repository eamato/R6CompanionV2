package eamato.funn.r6companion.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import eamato.funn.r6companion.MapsDetailsMasterCollectionQuery
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import eamato.funn.r6companion.data.entities.MapsRequestParams
import eamato.funn.r6companion.data.repositories.maps.IMapsRepository
import eamato.funn.r6companion.domain.entities.companion.maps.Map
import eamato.funn.r6companion.domain.mappers.companion.MapsUseCaseMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MapsPagingSource(private val mapsRepository: IMapsRepository) : PagingSource<Int, Map>() {

    private val logger = DefaultAppLogger.getInstance()

    private val mapsMapper = MapsUseCaseMapper

    override fun getRefreshKey(state: PagingState<Int, Map>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null

        val refreshKey =
            page.prevKey?.plus(state.config.pageSize) ?: page.nextKey?.minus(state.config.pageSize)

        logger.d(Message.message {
            clazz = this@MapsPagingSource::class.java
            message = "getRefreshKey refreshKey = $refreshKey"
        })

        return refreshKey
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Map> {
        val skip = params.key ?: 0
        val mapsCount = params.loadSize

        val mapsRequestParams = MapsRequestParams(skip = skip, limit = mapsCount)

        try {
            val response = mapsRepository.getMaps(mapsRequestParams)
            val maps = response?.items?.filterNotNull() ?: emptyList()
            val result = mapResultToMaps(maps)

            val prevKey = if (skip == 0) {
                null
            } else {
                skip - mapsCount
            }

            val nextKey = if (maps.isEmpty()) {
                null
            } else {
                skip + mapsCount
            }

            return LoadResult.Page(result, prevKey, nextKey)
        } catch (e: Exception) {
            logger.e(msg = Message.message {
                clazz = this@MapsPagingSource::class.java
                message = "load error"
            }, err = e)

            return LoadResult.Error(e)
        }
    }

    private suspend fun mapResultToMaps(
        maps: List<MapsDetailsMasterCollectionQuery.Item>
    ) = withContext(Dispatchers.IO) {
        maps.mapNotNull { mapItem -> mapsMapper.map(mapItem) }
    }
}
package eamato.funn.r6companion.domain.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import eamato.funn.r6companion.data.repositories.maps.IMapsRepository
import eamato.funn.r6companion.core.MAPS_COUNT_DEFAULT_VALUE
import eamato.funn.r6companion.core.MAPS_MAX_PAGE_SIZE
import eamato.funn.r6companion.core.MAPS_PREFETCH_DISTANCE
import eamato.funn.r6companion.domain.paging.MapsPagingSource
import javax.inject.Inject

class MapsListUseCase @Inject constructor(private val mapsRepository: IMapsRepository) {

    operator fun invoke() = Pager(
        config = PagingConfig(
            pageSize = MAPS_COUNT_DEFAULT_VALUE,
            prefetchDistance = MAPS_PREFETCH_DISTANCE,
            enablePlaceholders = true,
            initialLoadSize = MAPS_COUNT_DEFAULT_VALUE,
            maxSize = MAPS_MAX_PAGE_SIZE
        ),
        pagingSourceFactory = {
            MapsPagingSource(mapsRepository)
        }
    )
}
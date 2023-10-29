package eamato.funn.r6companion.domain.usecases

import eamato.funn.r6companion.data.repositories.maps.IMapsRepository
import eamato.funn.r6companion.MapsDetailsMasterCollectionQuery.Item
import eamato.funn.r6companion.domain.entities.companion.maps.Map
import eamato.funn.r6companion.core.utils.Result
import javax.inject.Inject

class MapsUseCase @Inject constructor(private val mapsRepository: IMapsRepository) {

    suspend operator fun invoke(mapper: IUseCaseMapper<Item, Map?>): Result<List<Map>> {
        val mapsResponse = mapsRepository.getMaps()
            ?: return Result.Error(Throwable("Something went wrong"))

        val maps = mapsResponse.items
            .filterNotNull()
            .mapNotNull { mapItem -> mapper.map(mapItem) }

        if (maps.isEmpty()) {
            Result.Error(Throwable("Something went wrong"))
        }

        return Result.Success(maps)
    }
}
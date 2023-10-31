package eamato.funn.r6companion.domain.usecases

import eamato.funn.r6companion.MapsDetailsMasterQuery.MapsDetailsMaster
import eamato.funn.r6companion.data.repositories.maps.IMapsRepository
import eamato.funn.r6companion.domain.entities.companion.maps.MapDetails
import eamato.funn.r6companion.core.utils.Result
import javax.inject.Inject

class MapDetailsUseCase @Inject constructor(private val mapsRepository: IMapsRepository) {

    suspend operator fun invoke(
        mapper: IUseCaseMapper<MapsDetailsMaster, MapDetails?>,
        mapId: String
    ): Result<MapDetails> {
        val mapDetailsResponse = mapsRepository.getMapDetails(mapId)
            ?: return Result.Error(Throwable("Something went wrong"))

        val mapDetails = mapper.map(mapDetailsResponse)
            ?: return Result.Error(Throwable("Something went wrong"))

        return Result.Success(mapDetails)
    }
}
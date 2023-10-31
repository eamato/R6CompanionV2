package eamato.funn.r6companion.data.repositories.maps

import eamato.funn.r6companion.MapsDetailsMasterCollectionQuery
import eamato.funn.r6companion.MapsDetailsMasterQuery
import eamato.funn.r6companion.data.sources.remote.maps.IRemoteDataSource
import javax.inject.Inject

class MapsRepository @Inject constructor(
    private val remoteDataSource: IRemoteDataSource
) : IMapsRepository {

    override suspend fun getMaps(): MapsDetailsMasterCollectionQuery.MapsDetailsMasterCollection? {
        return remoteDataSource.getMaps()
    }

    override suspend fun getMapDetails(mapId: String): MapsDetailsMasterQuery.MapsDetailsMaster? {
        return remoteDataSource.getMapDetails(mapId)
    }
}
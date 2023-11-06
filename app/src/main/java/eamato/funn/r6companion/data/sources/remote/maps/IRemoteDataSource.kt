package eamato.funn.r6companion.data.sources.remote.maps

import eamato.funn.r6companion.MapsDetailsMasterCollectionQuery
import eamato.funn.r6companion.MapsDetailsMasterQuery
import eamato.funn.r6companion.data.entities.MapsRequestParams

interface IRemoteDataSource {

    suspend fun getMaps(
        mapsRequestParams: MapsRequestParams
    ): MapsDetailsMasterCollectionQuery.MapsDetailsMasterCollection?

    suspend fun getMapDetails(mapId: String): MapsDetailsMasterQuery.MapsDetailsMaster?
}
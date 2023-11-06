package eamato.funn.r6companion.data.repositories.maps

import eamato.funn.r6companion.MapsDetailsMasterCollectionQuery
import eamato.funn.r6companion.MapsDetailsMasterQuery
import eamato.funn.r6companion.data.entities.MapsRequestParams

interface IMapsRepository {

    suspend fun getMaps(
        mapsRequestParams: MapsRequestParams
    ): MapsDetailsMasterCollectionQuery.MapsDetailsMasterCollection?

    suspend fun getMapDetails(mapId: String): MapsDetailsMasterQuery.MapsDetailsMaster?
}
package eamato.funn.r6companion.data.repositories.maps

import eamato.funn.r6companion.MapsDetailsMasterCollectionQuery
import eamato.funn.r6companion.MapsDetailsMasterQuery

interface IMapsRepository {

    suspend fun getMaps(): MapsDetailsMasterCollectionQuery.MapsDetailsMasterCollection?

    suspend fun getMapDetails(mapId: String): MapsDetailsMasterQuery.MapsDetailsMaster?
}
package eamato.funn.r6companion.data.sources.remote.maps

import eamato.funn.r6companion.MapsDetailsMasterCollectionQuery
import eamato.funn.r6companion.MapsDetailsMasterQuery

interface IRemoteDataSource {

    suspend fun getMaps(): MapsDetailsMasterCollectionQuery.MapsDetailsMasterCollection?

    suspend fun getMapDetails(mapId: String): MapsDetailsMasterQuery.MapsDetailsMaster?
}
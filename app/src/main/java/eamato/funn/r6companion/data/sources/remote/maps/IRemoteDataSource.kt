package eamato.funn.r6companion.data.sources.remote.maps

import eamato.funn.r6companion.MapsDetailsMasterCollectionQuery

interface IRemoteDataSource {

    suspend fun getMaps(): MapsDetailsMasterCollectionQuery.MapsDetailsMasterCollection?
}
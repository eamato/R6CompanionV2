package eamato.funn.r6companion.data.repositories.maps

import eamato.funn.r6companion.MapsDetailsMasterCollectionQuery

interface IMapsRepository {

    suspend fun getMaps(): MapsDetailsMasterCollectionQuery.MapsDetailsMasterCollection?
}
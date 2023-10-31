package eamato.funn.r6companion.data.sources.remote.maps

import com.apollographql.apollo3.ApolloClient
import eamato.funn.r6companion.MapsDetailsMasterCollectionQuery
import eamato.funn.r6companion.MapsDetailsMasterQuery
import javax.inject.Inject

class RemoteDataSourceMaps @Inject constructor(
    private val apolloClient: ApolloClient
) : IRemoteDataSource {

    override suspend fun getMaps(): MapsDetailsMasterCollectionQuery.MapsDetailsMasterCollection? {
        try {
            val response = apolloClient.query(MapsDetailsMasterCollectionQuery()).execute()
            val hasErrors = response.hasErrors()
            return response.data?.mapsDetailsMasterCollection
        } catch (e: Exception) {
            e.printStackTrace()
            // TODO add error handling
        }

        return null
    }

    override suspend fun getMapDetails(mapId: String): MapsDetailsMasterQuery.MapsDetailsMaster? {
        try {
            val response = apolloClient.query(MapsDetailsMasterQuery(mapId)).execute()
            val hasErrors = response.hasErrors()
            return response.data?.mapsDetailsMaster
        } catch (e: Exception) {
            e.printStackTrace()
            // TODO add error handling
        }

        return null
    }
}
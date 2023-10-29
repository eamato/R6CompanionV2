package eamato.funn.r6companion.data.sources.remote.maps

import com.apollographql.apollo3.ApolloClient
import eamato.funn.r6companion.MapsDetailsMasterCollectionQuery
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
}
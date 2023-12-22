package eamato.funn.r6companion.data.sources.remote.weapons

interface IRemoteDataSource {

    suspend fun getWeaponsPlaceHolder(): String
}
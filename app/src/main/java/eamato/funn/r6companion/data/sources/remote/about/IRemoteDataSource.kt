package eamato.funn.r6companion.data.sources.remote.about

import eamato.funn.r6companion.data.entities.AboutInfo

interface IRemoteDataSource {

    suspend fun getAboutInfo(): AboutInfo
}
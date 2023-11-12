package eamato.funn.r6companion.data.repositories.about

import eamato.funn.r6companion.data.entities.AboutInfo
import javax.inject.Inject
import eamato.funn.r6companion.data.sources.remote.about.IRemoteDataSource

class AboutRepository @Inject constructor(
    private val remoteDataSource: IRemoteDataSource
): IAboutRepository {

    override suspend fun getAboutInfo(): AboutInfo {
        return remoteDataSource.getAboutInfo()
    }
}
package eamato.funn.r6companion.data.repositories.weapons

import eamato.funn.r6companion.data.sources.remote.weapons.IRemoteDataSource
import javax.inject.Inject

class WeaponsRepository @Inject constructor(
    private val remoteDataSource: IRemoteDataSource
): IWeaponsRepository {

    override suspend fun getWeaponsPlaceHolder(): String {
        return remoteDataSource.getWeaponsPlaceHolder()
    }
}
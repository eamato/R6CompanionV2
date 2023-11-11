package eamato.funn.r6companion.data.repositories.about

import eamato.funn.r6companion.data.entities.AboutInfo

interface IAboutRepository {

    suspend fun getAboutInfo(): AboutInfo
}
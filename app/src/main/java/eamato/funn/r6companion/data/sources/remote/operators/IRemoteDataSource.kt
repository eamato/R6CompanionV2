package eamato.funn.r6companion.data.sources.remote.operators

import eamato.funn.r6companion.data.entities.Operators

interface IRemoteDataSource {

    suspend fun getOperators(): List<Operators.Operator>
}
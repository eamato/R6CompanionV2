package eamato.funn.r6companion.data.sources.local.operators

import eamato.funn.r6companion.data.entities.Operators

interface ILocalDataSource {

    suspend fun getOperators(): List<Operators.Operator>

    suspend fun saveLocalOperatorsIfNeeded(operators: List<Operators.Operator>) {}
}
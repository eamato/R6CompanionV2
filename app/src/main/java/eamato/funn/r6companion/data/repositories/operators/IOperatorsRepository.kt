package eamato.funn.r6companion.data.repositories.operators

import eamato.funn.r6companion.data.entities.Operators

interface IOperatorsRepository {

    suspend fun getOperators(): List<Operators.Operator>

    suspend fun getOperatorById(operatorId: Int): Operators.Operator?
}
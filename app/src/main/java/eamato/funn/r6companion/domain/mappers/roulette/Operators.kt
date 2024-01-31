package eamato.funn.r6companion.domain.mappers.roulette

import eamato.funn.r6companion.data.entities.Operators
import eamato.funn.r6companion.domain.entities.EOperatorRoles
import eamato.funn.r6companion.domain.entities.roulette.Operator
import eamato.funn.r6companion.domain.usecases.IUseCaseMapper

object RouletteOperatorUseCaseMapper : IUseCaseMapper<Operators.Operator, Operator?> {

    override fun map(input: Operators.Operator): Operator? {
        return input.toDomainOperator()
    }
}

private fun Operators.Operator.toDomainOperator(): Operator? {
    val id = this.id ?: return null
    val name = this.name ?: return null
    val iconLink = this.operatorIconLink ?: return null
    val imgLink = this.imgLink ?: return null
    val role = this.role?.let {
        when (it) {
            Operators.Operator.ROLE_ATTACKER -> EOperatorRoles.ATTACKERS
            Operators.Operator.ROLE_DEFENDER -> EOperatorRoles.DEFENDERS
            else -> EOperatorRoles.UNDEFINED
        }
    } ?: EOperatorRoles.UNDEFINED

    return Operator(id, name, iconLink, imgLink, role)
}
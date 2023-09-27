package eamato.funn.r6companion.domain.mappers.companion

import eamato.funn.r6companion.data.entities.Operators
import eamato.funn.r6companion.domain.entities.companion.operators.Operator
import eamato.funn.r6companion.domain.usecases.IUseCaseMapper

object CompanionOperatorUseCaseMapper : IUseCaseMapper<Operators.Operator, Operator?> {

    override fun map(input: Operators.Operator): Operator? {
        return input.toDomainOperator()
    }
}

private fun Operators.Operator.toDomainOperator(): Operator? {
    val id = this.id ?: return null
    val name = this.name ?: return null
    val iconLink = this.operatorIconLink ?: return null
    val imgLink = this.imgLink ?: return null
    val wideImgLink = this.wideImgLink ?: return null
    val armorRating = this.armorRating ?: return null
    val speedRating = this.speedRating ?: return null
    val role = this.role ?: return null

    val squad = this.squad?.let { squad ->
        val squadIconLink = squad.iconLink ?: return@let null
        val squadName = squad.name ?: return@let null

        Operator.Squad(iconLink = squadIconLink, name = squadName)
    } ?: return null

    val equipment = this.equipment?.let { equipment ->
        val devices = equipment.devices?.filterNotNull()?.mapNotNull devices@{ device ->
            val deviceIconLink = device.iconLink ?: return@devices null
            val deviceName = device.name ?: return@devices null

            Operator.Equipment.Device(iconLink = deviceIconLink, name = deviceName)
        } ?: return@let null

        val primaries = equipment.primaries?.filterNotNull()?.mapNotNull primaries@{ primary ->
            val primaryIconLink = primary.iconLink ?: return@primaries null
            val primaryName: String = primary.name ?: return@primaries null
            val primaryTypeText: String = primary.typeText ?: return@primaries null

            Operator.Equipment.Primary(
                iconLink = primaryIconLink,
                name = primaryName,
                typeText = primaryTypeText
            )
        } ?: return@let null

        val secondaries = equipment.secondaries?.filterNotNull()?.mapNotNull secondaries@{ secondary ->
            val secondaryIconLink = secondary.iconLink ?: return@secondaries null
            val secondaryName = secondary.name ?: return@secondaries null
            val secondaryTypeText = secondary.typeText ?: return@secondaries null

            Operator.Equipment.Secondary(
                iconLink = secondaryIconLink,
                name = secondaryName,
                typeText = secondaryTypeText
            )
        } ?: return@let null

        val skill = equipment.skill?.let skill@{ skill ->
            val skillIconLink = skill.iconLink ?: return@skill null
            val skillName = skill.name ?: return@skill null

            Operator.Equipment.Skill(iconLink = skillIconLink, name = skillName)
        } ?: return@let null

        Operator.Equipment(devices, primaries, secondaries, skill)
    } ?: return null

    return Operator(
        id = id,
        name = name,
        iconLink = iconLink,
        imgLink = imgLink,
        wideImgLink = wideImgLink,
        armorRating = armorRating,
        speedRating = speedRating,
        role = role,
        squad = squad,
        equipment = equipment
    )
}
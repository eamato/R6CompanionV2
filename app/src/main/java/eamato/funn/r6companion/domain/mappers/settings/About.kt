package eamato.funn.r6companion.domain.mappers.settings

import eamato.funn.r6companion.data.entities.AboutInfo
import eamato.funn.r6companion.domain.entities.settings.SettingsAboutInfo
import eamato.funn.r6companion.domain.usecases.IUseCaseMapper

object AboutInfoUseCaseMapper : IUseCaseMapper<AboutInfo, SettingsAboutInfo> {

    override fun map(input: AboutInfo): SettingsAboutInfo {
        return input.toDomainSettingsAboutInfo()
    }
}

private fun AboutInfo.toDomainSettingsAboutInfo(): SettingsAboutInfo {
    val ourMissionMessage = this.ourMission?.message ?: ""
    val ourMission = SettingsAboutInfo.OurMission(ourMissionMessage)

    val ourTeamMembers = this.aboutOurTeam
        ?.teamMembers
        ?.filterNotNull()
        ?.map { teamMember ->
            val firstName = teamMember.firstName ?: ""
            val lastName = teamMember.lastName ?: ""
            val imageUrl = teamMember.image
            val positions = teamMember.positions?.filterNotNull() ?: emptyList()

            SettingsAboutInfo.OurTeam.TeamMember(
                firstName = firstName,
                lastName = lastName,
                imageUrl = imageUrl,
                positions = positions
            )
        }
        ?: emptyList()
    val ourTeam = SettingsAboutInfo.OurTeam(ourTeamMembers)

    return SettingsAboutInfo(ourMission = ourMission, ourTeam = ourTeam)
}
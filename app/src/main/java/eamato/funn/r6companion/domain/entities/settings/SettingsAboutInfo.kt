package eamato.funn.r6companion.domain.entities.settings

data class SettingsAboutInfo(
    val ourMission: OurMission,
    val ourTeam: OurTeam
) {
    data class OurMission(val missionText: String)

    data class OurTeam(val teamMembers: List<TeamMember>) {

        data class TeamMember(
            val firstName: String,
            val lastName: String,
            val imageUrl: String?,
            val positions: List<String>
        )
    }
}
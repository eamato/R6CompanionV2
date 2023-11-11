package eamato.funn.r6companion.data.entities

import com.google.gson.annotations.SerializedName

data class AboutInfo(
    val ourMission: AboutOurMission?,
    val aboutOurTeam: AboutOurTeam?
) {
    data class AboutOurMission(
        @SerializedName("message")
        val message: String?
    )

    data class AboutOurTeam(
        @SerializedName("positions")
        val teamMembers: List<TeamMember?>?
    ) {

        data class TeamMember(
            @SerializedName("first_name")
            val firstName: String? = null,
            @SerializedName("image")
            val image: String? = null,
            @SerializedName("last_name")
            val lastName: String? = null,
            @SerializedName("positions")
            val positions: List<String?>? = null
        )
    }
}
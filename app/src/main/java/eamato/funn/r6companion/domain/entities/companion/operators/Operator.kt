package eamato.funn.r6companion.domain.entities.companion.operators

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Operator(
    val id: Int,
    val name: String,
    val iconLink: String,
    val imgLink: String,
    val wideImgLink: String,
    val armorRating: Int,
    val speedRating: Int,
    val role: String,
    val squad: Squad,
    val equipment: Equipment,
) : Parcelable {

    companion object {
        const val ROLE_DEFENDER = "DEFENDER"
        const val ROLE_ATTACKER = "ATTACKER"
    }

    @Parcelize
    data class Squad(
        val iconLink: String,
        val name: String
    ) : Parcelable

    @Parcelize
    data class Equipment(
        val devices: List<Device>,
        val primaries: List<Primary>,
        val secondaries: List<Secondary>,
        val skill: Skill
    ) : Parcelable {

        @Parcelize
        data class Device(
            val iconLink: String,
            val name: String
        ) : Parcelable

        @Parcelize
        data class Primary(
            val iconLink: String,
            val name: String,
            val typeText: String
        ) : Parcelable

        @Parcelize
        data class Secondary(
            val iconLink: String,
            val name: String,
            val typeText: String
        ) : Parcelable

        @Parcelize
        data class Skill(
            val iconLink: String,
            val name: String
        ) : Parcelable
    }
}
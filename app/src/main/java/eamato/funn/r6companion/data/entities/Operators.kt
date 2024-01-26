package eamato.funn.r6companion.data.entities

import com.google.gson.annotations.SerializedName

data class Operators(
    @SerializedName("operators")
    val operators: List<Operator?>?
) {
    data class Operator(
        @SerializedName("armor_rating")
        val armorRating: Int?,
        @SerializedName("equipment")
        val equipment: Equipment?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("img_link")
        val imgLink: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("operator_icon_link")
        val operatorIconLink: String?,
        @SerializedName("role")
        val role: String?,
        @SerializedName("speed_rating")
        val speedRating: Int?,
        @SerializedName("squad")
        val squad: Squad?,
        @SerializedName("wide_img_link")
        val wideImgLink: String?
    ) {

        companion object {
            const val ROLE_DEFENDER = "DEFENDER"
            const val ROLE_ATTACKER = "ATTACKER"
        }

        data class Equipment(
            @SerializedName("devices")
            val devices: List<Device?>?,
            @SerializedName("primaries")
            val primaries: List<Primary?>?,
            @SerializedName("secondaries")
            val secondaries: List<Secondary?>?,
            @SerializedName("skill")
            val skill: Skill?
        ) {
            data class Device(
                @SerializedName("icon_link")
                val iconLink: String?,
                @SerializedName("name")
                val name: String?
            )

            data class Primary(
                @SerializedName("icon_link")
                val iconLink: String?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("type_text")
                val typeText: String?
            )

            data class Secondary(
                @SerializedName("icon_link")
                val iconLink: String?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("type_text")
                val typeText: String?
            )

            data class Skill(
                @SerializedName("description")
                val description: String?,
                @SerializedName("icon_link")
                val iconLink: String?,
                @SerializedName("name")
                val name: String?
            )
        }

        data class Squad(
            @SerializedName("icon_link")
            val iconLink: String?,
            @SerializedName("name")
            val name: String?
        )
    }
}
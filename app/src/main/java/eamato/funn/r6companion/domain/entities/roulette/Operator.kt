package eamato.funn.r6companion.domain.entities.roulette

import android.os.Parcelable
import eamato.funn.r6companion.domain.entities.EOperatorRoles
import kotlinx.parcelize.Parcelize

@Parcelize
data class Operator(
    val id: Int,
    val name: String,
    val iconLink: String,
    val imgLink: String,
    val role: EOperatorRoles
) : Parcelable
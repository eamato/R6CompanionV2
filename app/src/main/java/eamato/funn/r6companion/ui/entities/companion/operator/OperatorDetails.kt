package eamato.funn.r6companion.ui.entities.companion.operator

import eamato.funn.r6companion.core.utils.UiText

interface IViewType {
    fun getItemViewType(): Int
}

sealed class OperatorDetails : IViewType {

    companion object {
        const val VIEW_TYPE_IMAGE = 1
        const val VIEW_TYPE_TITLE = 2
        const val VIEW_TYPE_SUBTITLE = 3
        const val VIEW_TYPE_TEXT = 4
        const val VIEW_TYPE_STAT = 5
        const val VIEW_TYPE_LOAD_OUT_ENTITY = 6
        const val VIEW_TYPE_DIVIDER = 7
        const val VIEW_TYPE_ABILITY_ENTITY = 8
        const val VIEW_TYPE_ORGANIZATION_ENTITY = 9
    }

    override fun getItemViewType(): Int {
        return when (this) {
            is OperatorDetailsImage -> VIEW_TYPE_IMAGE
            is OperatorDetailsTitle -> VIEW_TYPE_TITLE
            is OperatorDetailsSubtitle -> VIEW_TYPE_SUBTITLE
            is OperatorDetailsText -> VIEW_TYPE_TEXT
            is OperatorDetailsStat -> VIEW_TYPE_STAT
            is OperatorDetailsLoadOutEntity -> VIEW_TYPE_LOAD_OUT_ENTITY
            is OperatorDetailsAbilityEntity -> VIEW_TYPE_ABILITY_ENTITY
            is OperatorDetailsDivider -> VIEW_TYPE_DIVIDER
            is OrganizationEntity -> VIEW_TYPE_ORGANIZATION_ENTITY
        }
    }

    data class OperatorDetailsImage(val imageUrl: UiText) : OperatorDetails()
    data class OperatorDetailsTitle(val title: UiText) : OperatorDetails()
    data class OperatorDetailsSubtitle(val subtitle: UiText) : OperatorDetails()
    data class OperatorDetailsText(val text: UiText) : OperatorDetails()
    data class OperatorDetailsStat(val name: UiText, val value: Int) : OperatorDetails()
    data class OperatorDetailsLoadOutEntity(
        val name: UiText,
        val imageUrl: UiText,
        val typeText: UiText? = null
    ) : OperatorDetails()
    data class OperatorDetailsAbilityEntity(
        val name: UiText,
        val imageUrl: UiText
    ) : OperatorDetails()
    object OperatorDetailsDivider : OperatorDetails()
    data class OrganizationEntity(val name: UiText, val imageUrl: UiText) : OperatorDetails()
}

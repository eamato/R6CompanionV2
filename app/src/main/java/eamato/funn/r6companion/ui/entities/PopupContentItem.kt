package eamato.funn.r6companion.ui.entities

import androidx.annotation.DrawableRes
import eamato.funn.r6companion.core.utils.UiText

data class PopupContentItem(
    @DrawableRes val icon: Int?,
    val title: UiText,
    val subTitle: UiText?,
    var isEnabled: Boolean = true,
    val onClickListener: (() -> Unit)?
)

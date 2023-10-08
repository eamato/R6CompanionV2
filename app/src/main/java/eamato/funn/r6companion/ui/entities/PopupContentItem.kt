package eamato.funn.r6companion.ui.entities

import androidx.annotation.DrawableRes
import eamato.funn.r6companion.core.utils.UiText
import java.io.Serializable

data class PopupContentItem(
    @DrawableRes val icon: Int?,
    val title: UiText,
    val subTitle: UiText?,
    val onClickListener: (() -> Unit)?
) : Serializable

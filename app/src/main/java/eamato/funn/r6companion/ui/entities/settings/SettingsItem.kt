package eamato.funn.r6companion.ui.entities.settings

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import eamato.funn.r6companion.core.utils.UiText
import eamato.funn.r6companion.ui.entities.IViewType
import eamato.funn.r6companion.ui.entities.PopupContentItem

sealed class SettingsItem(
    @DrawableRes open val icon: Int,
    @StringRes open val title: Int,
    open val subTitle: UiText?,
    open val isEnabled: Boolean = true
) : IViewType {

    companion object {
        const val VIEW_TYPE_POPUP = 1
        const val VIEW_TYPE_SWITCH = 2
        const val VIEW_TYPE_SCREEN = 3
    }

    override fun getItemViewType(): Int {
        return when (this) {
            is SettingsItemPopup -> VIEW_TYPE_POPUP
            is SettingsItemSwitch -> VIEW_TYPE_SWITCH
            is SettingsItemScreen -> VIEW_TYPE_SCREEN
        }
    }

    data class SettingsItemPopup(
        @DrawableRes override val icon: Int,
        @StringRes override val title: Int,
        override val subTitle: UiText?,
        override val isEnabled: Boolean = true,
        val popupContentItems: List<PopupContentItem>
    ) : SettingsItem(
        icon = icon,
        title = title,
        subTitle = subTitle,
        isEnabled = isEnabled
    )

    data class SettingsItemSwitch(
        @DrawableRes override val icon: Int,
        @StringRes override val title: Int,
        override val subTitle: UiText?,
        val isChecked: Boolean,
        override val isEnabled: Boolean = true,
        val onCheckedListener: (Boolean) -> Unit
    ) : SettingsItem(
        icon = icon,
        title = title,
        subTitle = subTitle,
        isEnabled = isEnabled
    )

    data class SettingsItemScreen(
        @DrawableRes override val icon: Int,
        @StringRes override val title: Int,
        @IdRes val destinationId: Int,
        val args: Bundle?
    ) : SettingsItem(
        icon = icon,
        title = title,
        subTitle = null,
        isEnabled = true
    )
}

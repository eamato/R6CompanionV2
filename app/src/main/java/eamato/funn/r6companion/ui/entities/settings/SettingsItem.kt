package eamato.funn.r6companion.ui.entities.settings

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import eamato.funn.r6companion.core.utils.UiText
import eamato.funn.r6companion.ui.entities.IViewType
import eamato.funn.r6companion.ui.entities.PopupContentItem
import kotlin.reflect.KClass

sealed class SettingsItem(
    open val id: String,
    @DrawableRes open val icon: Int,
    @StringRes open val title: Int,
    open val subTitle: UiText?,
    open val isEnabled: Boolean = true,
    open val isSelectable: Boolean = false
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
        override val id: String,
        @DrawableRes override val icon: Int,
        @StringRes override val title: Int,
        override val subTitle: UiText?,
        override val isEnabled: Boolean = true,
        val popupContentItems: List<PopupContentItem>
    ) : SettingsItem(
        id = id,
        icon = icon,
        title = title,
        subTitle = subTitle,
        isEnabled = isEnabled,
        isSelectable = true
    )

    data class SettingsItemSwitch(
        override val id: String,
        @DrawableRes override val icon: Int,
        @StringRes override val title: Int,
        override val subTitle: UiText?,
        val isChecked: Boolean,
        override val isEnabled: Boolean = true,
        val onCheckedListener: (Boolean) -> Unit
    ) : SettingsItem(
        id = id,
        icon = icon,
        title = title,
        subTitle = subTitle,
        isEnabled = isEnabled
    )

    data class SettingsItemScreen(
        override val id: String,
        @DrawableRes override val icon: Int,
        @StringRes override val title: Int,
        @IdRes val destinationId: Int,
        val destinationClass: KClass<out Fragment>,
        val args: Bundle?
    ) : SettingsItem(
        id = id,
        icon = icon,
        title = title,
        subTitle = null,
        isEnabled = true,
        isSelectable = true
    )
}

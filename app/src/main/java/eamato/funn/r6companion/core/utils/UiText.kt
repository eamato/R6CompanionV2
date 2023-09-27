package eamato.funn.r6companion.core.utils

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {
    data class SimpleString(val value: String?): UiText()
    class ResourceString(@StringRes val resId: Int, vararg val args: Any) : UiText()

    fun asString(context: Context): String? {
        return when (this) {
            is SimpleString -> value
            is ResourceString -> context.getString(resId, args)
        }
    }
}
package eamato.funn.r6companion.core.extenstions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

fun View.setViewsEnabled(isEnabled: Boolean) {
    if (this is ViewGroup) {
        children.forEach { it.setViewsEnabled(isEnabled) }
    }

    this.isEnabled = isEnabled
}
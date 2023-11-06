package eamato.funn.r6companion.core.extenstions

import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children

fun View.setViewsEnabled(isEnabled: Boolean) {
    if (this is ViewGroup) {
        children.forEach { it.setViewsEnabled(isEnabled) }
    }

    this.isEnabled = isEnabled
}

fun View?.applySystemInsetsIfNeeded(onInsetsFound: (Insets) -> Unit) {
    if (this == null) {
        return
    }

    ViewCompat.setOnApplyWindowInsetsListener(this) { _, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        if (insets.top > 0 || insets.left > 0 || insets.right > 0 || insets.bottom > 0) {
            onInsetsFound.invoke(insets)
        }

        WindowInsetsCompat.CONSUMED
    }
}

fun View.setViewVisibleOrGone(visibility: Boolean) {
    this.visibility = if (visibility) {
        View.VISIBLE
    } else {
        View.GONE
    }
}
package eamato.funn.r6companion.ui.dialogs

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import eamato.funn.r6companion.core.extenstions.isLandscape

object DialogDefaultPopupManager {

    fun create(context: Context): IDialogDefault {
        if (context.isLandscape()) {
            return DialogDefaultAppPopupSide(context)
        }

        return DialogDefaultAppPopup()
    }
}
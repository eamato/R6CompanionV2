package eamato.funn.r6companion.ui.dialogs

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import eamato.funn.r6companion.core.extenstions.isLandscape

class DialogDefaultPopupManager(private val lifecycleOwner: LifecycleOwner) {

    private var dialog: IDialogDefault? = null

    private var observer: Observer? = null

    init {
        if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            dialog?.dismiss()
            dialog = null
            observer?.run { lifecycleOwner.lifecycle.removeObserver(this) }
            observer = null
        }

        observer?.run { lifecycleOwner.lifecycle.removeObserver(this) }
        val observer = Observer()
        lifecycleOwner.lifecycle.addObserver(observer)
        this.observer = observer
    }

    fun create(context: Context): IDialogDefault {
        if (context.isLandscape()) {
            dialog?.dismiss()
            dialog = null

            return DialogDefaultAppPopupSide(context).also { dialog = it }
        }

        dialog?.dismiss()
        dialog = null

        return DialogDefaultAppPopup().also { dialog = it }
    }

    fun dismiss() {
        dialog?.dismiss()
    }

    private inner class Observer : LifecycleEventObserver {

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                dialog?.dismiss()
                dialog = null

                observer?.run { lifecycleOwner.lifecycle.removeObserver(this) }
                observer = null
            }
        }
    }
}
package eamato.funn.r6companion.ui.entities.news.details

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

abstract class AContentView : IContentView {

    abstract fun onCreateView(parent: ViewGroup): View?
    abstract fun onDestroy()

    private var observer: Observer? = null

    override fun createView(parent: ViewGroup, lifecycleOwner: LifecycleOwner): View? {
        if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            onDestroy()
            observer?.run { lifecycleOwner.lifecycle.removeObserver(this) }
            return null
        }

        observer?.run { lifecycleOwner.lifecycle.removeObserver(this) }
        val observer = createObserver()
        lifecycleOwner.lifecycle.addObserver(observer)
        this.observer = observer

        return onCreateView(parent)
    }

    protected open fun createObserver(): Observer {
        return Observer()
    }

    protected open inner class Observer : LifecycleEventObserver {

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                onDestroy()
            }
        }
    }
}
package eamato.funn.r6companion.ui.entities.news.details

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

abstract class AContentViewVideo : AContentView() {

    abstract fun onPause()
    abstract fun onResume()

    protected var appContext: Context? = null

    override fun createObserver(): AContentView.Observer {
        return Observer()
    }

    fun setApplicationContext(applicationContext: Context?) {
        appContext = applicationContext
    }

    private inner class Observer : AContentView.Observer() {

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_DESTROY -> onDestroy()
                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> onPause()
                Lifecycle.Event.ON_RESUME -> onResume()
                else -> {}
            }
        }
    }
}
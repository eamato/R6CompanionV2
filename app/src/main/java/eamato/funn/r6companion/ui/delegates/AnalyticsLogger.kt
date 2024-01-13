package eamato.funn.r6companion.ui.delegates

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message

interface IAnalyticsLogger {

    fun registerLifecycleOwner(owner: LifecycleOwner, context: Context)
}

class AnalyticsLogger(
    private val className: String,
    private val screenName: String
) : IAnalyticsLogger, LifecycleEventObserver {

    private var firebaseAnalytics: FirebaseAnalytics? = null

    private val logger = DefaultAppLogger.getInstance()

    override fun registerLifecycleOwner(owner: LifecycleOwner, context: Context) {
        owner.lifecycle.removeObserver(this)
        owner.lifecycle.addObserver(this)

        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                logger.i(Message.message {
                    clazz = this@AnalyticsLogger::class.java
                    message = "Screen view event: Name = $screenName, Class = $className"
                })

                firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                    param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                    param(FirebaseAnalytics.Param.SCREEN_CLASS, className)
                }
            }
            else -> Unit
        }
    }
}
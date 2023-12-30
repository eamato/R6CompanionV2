package eamato.funn.r6companion.core.utils.logger

import android.util.Log
import eamato.funn.r6companion.BuildConfig
import eamato.funn.r6companion.core.LOGGER_TAG

interface ILogger {

    fun d(msg: Message? = null, err: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            Log.d(LOGGER_TAG, msg?.string(), err)
        }
    }

    fun i(msg: Message? = null, err: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            Log.i(LOGGER_TAG, msg?.string(), err)
        }
    }

    fun e(msg: Message? = null, err: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            Log.e(LOGGER_TAG, msg?.string(), err)
        }
    }
}
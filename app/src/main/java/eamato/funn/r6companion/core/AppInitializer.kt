package eamato.funn.r6companion.core

import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.firebase.messaging.FirebaseMessaging
import eamato.funn.r6companion.core.extenstions.log
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import eamato.funn.r6companion.data.FirebaseRemoteConfigService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class AppInitializer @Inject constructor(
    private val firebaseRemoteConfigService: FirebaseRemoteConfigService,
    private val context: Context
) {

    private val logger = DefaultAppLogger.getInstance()

    class AppInitializationResult {
        var success: Boolean = false
            internal set
        var notificationToken: String? = null
            internal set
    }

    suspend fun initApp(): AppInitializationResult = withContext(Dispatchers.IO) {
        val appInitializationResult = AppInitializationResult()

        val remoteConfigRequest = async { firebaseRemoteConfigService.refresh() }
        val mobileAdsSDKInitializationRequest = async { initializeMobilAdsSDK() }
        val notificationTokenRequest = FirebaseMessaging.getInstance().token.asDeferred()

        try {
            withTimeout(5 * 1_000L) {
                remoteConfigRequest.await()
                mobileAdsSDKInitializationRequest.await()
                appInitializationResult.notificationToken = notificationTokenRequest.await()
            }
            appInitializationResult.success = true
        } catch (e: Exception) {
            e.printStackTrace()
            e.log(logger)
            if (e is TimeoutCancellationException) {
                logger.d(Message.message {
                    clazz = this@AppInitializer::class.java
                    message = "Something reached timeout"
                })
            }
        }

        appInitializationResult
    }

    private suspend fun initializeMobilAdsSDK() = withContext(Dispatchers.IO) {
        logger.d(Message.message {
            clazz = this@AppInitializer::class.java
            message = "initializeMobilAdsSDK"
        })
        return@withContext MobileAds.initialize(context) {
            logger.d(Message.message {
                clazz = this@AppInitializer::class.java
                message = "mobile ads initialize ${it.adapterStatusMap.values.joinToString(
                    transform = { it.initializationState.name }
                )}"
            })
        }
    }
}
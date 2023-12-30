package eamato.funn.r6companion.data.sources.remote.weapons

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import eamato.funn.r6companion.core.FIREBASE_REMOTE_CONFIG_COMING_SOON
import eamato.funn.r6companion.core.extenstions.log
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import eamato.funn.r6companion.data.FirebaseRemoteConfigService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class RemoteDataSourceWeapons @Inject constructor(
    private val firebaseRemoteConfigService: FirebaseRemoteConfigService
) : IRemoteDataSource {

    private val logger = DefaultAppLogger.getInstance()

    override suspend fun getWeaponsPlaceHolder(): String {
        return withContext(Dispatchers.IO) {
            var comingSoonText = firebaseRemoteConfigService.getRemoteConfigStringValue(
                FIREBASE_REMOTE_CONFIG_COMING_SOON
            )

            val needToTryReInit = comingSoonText == FirebaseRemoteConfig.DEFAULT_VALUE_FOR_STRING

            if (needToTryReInit) {
                try {
                    withTimeout(5 * 1_000L) {
                        firebaseRemoteConfigService.refresh()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    e.log(logger)

                    if (e is TimeoutCancellationException) {
                        logger.d(Message.message {
                            clazz = this@RemoteDataSourceWeapons::class.java
                            message = "Something reached timeout"
                        })
                    }
                }

                comingSoonText = firebaseRemoteConfigService.getRemoteConfigStringValue(
                    FIREBASE_REMOTE_CONFIG_COMING_SOON
                )
            }

            return@withContext comingSoonText
        }
    }
}
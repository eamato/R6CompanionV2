package eamato.funn.r6companion.data

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseRemoteConfigService @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) {

    private val logger = DefaultAppLogger.getInstance()

    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        logger.i(Message.message {
                            clazz = this@FirebaseRemoteConfigService::class.java
                            message = "fetchAndActivate successful"
                        })
                    } else {
                        logger.i(Message.message {
                            clazz = this@FirebaseRemoteConfigService::class.java
                            message = "fetchAndActivate unsuccessful"
                        })
                    }
                }
                .addOnCanceledListener {
                    logger.i(Message.message {
                        clazz = this@FirebaseRemoteConfigService::class.java
                        message = "fetchAndActivate canceled"
                    })
                }
                .addOnFailureListener {
                    logger.i(Message.message {
                        clazz = this@FirebaseRemoteConfigService::class.java
                        message = "fetchAndActivate failure"
                    })
                }
        }
    }

    fun getRemoteConfigStringValue(key: String) = firebaseRemoteConfig.getString(key)
}
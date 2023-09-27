package eamato.funn.r6companion.data.sources.remote.operators

import com.google.firebase.remoteconfig.FirebaseRemoteConfig.DEFAULT_VALUE_FOR_STRING
import eamato.funn.r6companion.core.FIREBASE_REMOTE_CONFIG_OPERATORS
import eamato.funn.r6companion.core.extenstions.fromJson
import eamato.funn.r6companion.core.extenstions.log
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import eamato.funn.r6companion.data.FirebaseRemoteConfigService
import eamato.funn.r6companion.data.entities.Operators
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class RemoteDataSourceFirebaseRemoteConfig @Inject constructor(
    private val firebaseRemoteConfigService: FirebaseRemoteConfigService
) : IRemoteDataSource {

    private val logger = DefaultAppLogger.getInstance()

    override suspend fun getOperators(): List<Operators.Operator> {
        return withContext(Dispatchers.IO) {
            var operatorsString = firebaseRemoteConfigService.getRemoteConfigStringValue(
                FIREBASE_REMOTE_CONFIG_OPERATORS
            )
            if (operatorsString == DEFAULT_VALUE_FOR_STRING) {
                try {
                    withTimeout(5 * 1_000L) {
                        firebaseRemoteConfigService.refresh()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    e.log(logger)

                    if (e is TimeoutCancellationException) {
                        logger.d(Message.message {
                            clazz = this@RemoteDataSourceFirebaseRemoteConfig::class.java
                            message = "Something reached timeout"
                        })
                    }
                }

                operatorsString = firebaseRemoteConfigService.getRemoteConfigStringValue(
                    FIREBASE_REMOTE_CONFIG_OPERATORS
                )
            }

            return@withContext operatorsString
                .fromJson(Operators::class.java)
                ?.operators
                ?.filterNotNull()
                ?: emptyList()
        }
    }
}
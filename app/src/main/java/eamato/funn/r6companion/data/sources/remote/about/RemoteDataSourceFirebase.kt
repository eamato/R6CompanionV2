package eamato.funn.r6companion.data.sources.remote.about

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import eamato.funn.r6companion.core.FIREBASE_REMOTE_CONFIG_OUR_MISSION
import eamato.funn.r6companion.core.FIREBASE_REMOTE_CONFIG_OUR_TEAM
import eamato.funn.r6companion.core.extenstions.fromJson
import eamato.funn.r6companion.core.extenstions.log
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import eamato.funn.r6companion.data.FirebaseRemoteConfigService
import eamato.funn.r6companion.data.entities.AboutInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class RemoteDataSourceFirebase @Inject constructor(
    private val firebaseRemoteConfigService: FirebaseRemoteConfigService
) : IRemoteDataSource {

    private val logger = DefaultAppLogger.getInstance()

    override suspend fun getAboutInfo(): AboutInfo {
        return withContext(Dispatchers.IO) {
            var ourMissionText = firebaseRemoteConfigService.getRemoteConfigStringValue(
                FIREBASE_REMOTE_CONFIG_OUR_MISSION
            )
            var ourTeamText = firebaseRemoteConfigService.getRemoteConfigStringValue(
                FIREBASE_REMOTE_CONFIG_OUR_TEAM
            )

            val needToTryReInit = ourMissionText == FirebaseRemoteConfig.DEFAULT_VALUE_FOR_STRING ||
                    ourTeamText == FirebaseRemoteConfig.DEFAULT_VALUE_FOR_STRING

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
                            clazz = this@RemoteDataSourceFirebase::class.java
                            message = "Something reached timeout"
                        })
                    }
                }

                ourMissionText = firebaseRemoteConfigService.getRemoteConfigStringValue(
                    FIREBASE_REMOTE_CONFIG_OUR_MISSION
                )
                ourTeamText = firebaseRemoteConfigService.getRemoteConfigStringValue(
                    FIREBASE_REMOTE_CONFIG_OUR_TEAM
                )
            }

            return@withContext AboutInfo(
                ourMission = ourMissionText.fromJson(AboutInfo.AboutOurMission::class.java),
                aboutOurTeam = ourTeamText.fromJson(AboutInfo.AboutOurTeam::class.java)
            )
        }
    }
}
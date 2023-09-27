package eamato.funn.r6companion.data.repositories.operators

import android.content.Context
import eamato.funn.r6companion.core.extenstions.isCurrentlyConnectedToInternet
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import eamato.funn.r6companion.data.entities.Operators
import eamato.funn.r6companion.data.sources.local.operators.ILocalDataSource
import eamato.funn.r6companion.data.sources.remote.operators.IRemoteDataSource
import javax.inject.Inject

class OperatorsRepository @Inject constructor(
    private val context: Context,
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource,
    private val lastChanceLocalDataSource: ILocalDataSource
) : IOperatorsRepository {

    private val logger = DefaultAppLogger.getInstance()

    override suspend fun getOperators(): List<Operators.Operator> {
        var operatorsList = emptyList<Operators.Operator>()

        if (context.isCurrentlyConnectedToInternet()) {
            operatorsList = remoteDataSource.getOperators()
            if (operatorsList.isNotEmpty()) {
                logger.d(Message.message {
                    clazz = this@OperatorsRepository::class.java
                    message = "Remote data source operators received"
                })
                localDataSource.saveLocalOperatorsIfNeeded(operatorsList)
            }
        }

        if (operatorsList.isEmpty()) {
            operatorsList = localDataSource.getOperators()
            if (operatorsList.isNotEmpty()) {
                logger.d(Message.message {
                    clazz = this@OperatorsRepository::class.java
                    message = "Local file data source operators received"
                })
            }
        }

        if (operatorsList.isEmpty()) {
            operatorsList = lastChanceLocalDataSource.getOperators()
            if (operatorsList.isNotEmpty()) {
                logger.d(Message.message {
                    clazz = this@OperatorsRepository::class.java
                    message = "Assets data source operators received"
                })
            }
        }

        return operatorsList
    }
}
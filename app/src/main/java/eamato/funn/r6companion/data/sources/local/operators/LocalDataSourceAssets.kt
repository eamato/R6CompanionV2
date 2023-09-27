package eamato.funn.r6companion.data.sources.local.operators

import android.content.Context
import com.google.gson.Gson
import eamato.funn.r6companion.core.OPERATORS_ASSETS_FILE_NAME
import eamato.funn.r6companion.core.extenstions.log
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.data.entities.Operators
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class LocalDataSourceAssets @Inject constructor(
    private val context: Context
) : ILocalDataSource {

    private val logger = DefaultAppLogger.getInstance()

    override suspend fun getOperators(): List<Operators.Operator> {
        return withContext(Dispatchers.IO) {
            try {
                val assetsReader = BufferedReader(
                    InputStreamReader(context.assets.open(OPERATORS_ASSETS_FILE_NAME))
                )
                val operators = Gson().fromJson(assetsReader, Operators::class.java)

                return@withContext operators?.operators?.filterNotNull() ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                e.log(logger)

                return@withContext emptyList()
            }
        }
    }
}
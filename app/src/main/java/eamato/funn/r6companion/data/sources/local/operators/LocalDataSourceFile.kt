package eamato.funn.r6companion.data.sources.local.operators

import android.content.Context
import com.google.gson.Gson
import eamato.funn.r6companion.core.OPERATORS_LOCAL_FILE_NAME
import eamato.funn.r6companion.core.extenstions.fromJson
import eamato.funn.r6companion.core.extenstions.log
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import eamato.funn.r6companion.data.entities.Operators
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class LocalDataSourceFile @Inject constructor(
    private val context: Context
) : ILocalDataSource {

    private val logger = DefaultAppLogger.getInstance()

    override suspend fun getOperators(): List<Operators.Operator> {
        return withContext(Dispatchers.IO) {
            val operatorsString = readLocalOperatorsFileData()
                ?: return@withContext emptyList()

            return@withContext operatorsString
                .fromJson(Operators::class.java)
                ?.operators
                ?.filterNotNull()
                ?: emptyList()
        }
    }

    override suspend fun saveLocalOperatorsIfNeeded(operators: List<Operators.Operator>) {
        val localStoredOperators = getOperators()
        if (localStoredOperators.isEmpty() || (localStoredOperators != operators)) {
            logger.d(Message.message {
                clazz = this@LocalDataSourceFile::class.java
                message = "Saving remote operators to local file"
            })
            try {
                getLocalOperatorsFile()?.writeText(Gson().toJson(Operators(operators)))
            } catch (e: Exception) {
                e.printStackTrace()
                e.log(logger)
            }
            return
        }
    }

    private suspend fun readLocalOperatorsFileData(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val localOperatorsFile = getLocalOperatorsFile() ?: return@withContext null
                val isNewFile = localOperatorsFile.createNewFile()
                if (isNewFile) {
                    return@withContext null
                } else {
                    return@withContext localOperatorsFile
                        .readText()
                        .takeIf { it.isNotEmpty() && it.isNotBlank() }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                e.log(logger)

                return@withContext null
            }
        }
    }

    private fun getLocalOperatorsFile(): File? {
        val filesDir = context.filesDir
        if (filesDir.exists().not() || filesDir.isDirectory.not()) {
            return null
        }
        return File(filesDir, OPERATORS_LOCAL_FILE_NAME)
    }
}
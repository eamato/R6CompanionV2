package eamato.funn.r6companion.core.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import eamato.funn.r6companion.SavedOperator
import eamato.funn.r6companion.SavedOperators
import eamato.funn.r6companion.domain.entities.companion.operators.SavedOperatorsSerializer
import eamato.funn.r6companion.domain.entities.roulette.Operator
import javax.inject.Inject

class SavedOperatorsManager @Inject constructor(private val context: Context) {

    private companion object {
        val Context.dataStore: DataStore<SavedOperators> by dataStore(
            fileName = "saved_operators.pb",
            serializer = SavedOperatorsSerializer,
        )
    }

    val savedOperators = context.dataStore.data

    suspend fun saveOperators(selectedOperators: List<Operator>) {
        context.dataStore.updateData { preferences ->
            val savedOperatorsBuilder = preferences.toBuilder().apply { clearSavedOperators() }

            selectedOperators.forEachIndexed { index, operator ->
                val savedOperatorBuilder = SavedOperator.newBuilder()
                savedOperatorBuilder.id = operator.id
                savedOperatorBuilder.name = operator.name
                savedOperatorBuilder.iconLink = operator.iconLink
                savedOperatorBuilder.imageLink = operator.imgLink

                savedOperatorsBuilder.addSavedOperators(index, savedOperatorBuilder)
            }

            savedOperatorsBuilder.build()
        }
    }

    suspend fun clearSavedOperators() {
        context.dataStore.updateData { preferences ->
            preferences.toBuilder().clearSavedOperators().build()
        }
    }
}
package eamato.funn.r6companion.core.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import eamato.funn.r6companion.SavedOperator
import eamato.funn.r6companion.domain.entities.companion.operators.SavedOperatorSerializer
import javax.inject.Inject

class SavedOperatorsManager @Inject constructor(private val context: Context) {

    private companion object {
        val Context.dataStore: DataStore<SavedOperator> by dataStore(
            fileName = "saved_operators.pb",
            serializer = SavedOperatorSerializer,
        )
    }

    val savedOperators = context.dataStore.data

    suspend fun saveOperator() {
        context.dataStore.updateData { preferences ->
            preferences.toBuilder()
                .setId(1)
                .build()
        }
    }
}
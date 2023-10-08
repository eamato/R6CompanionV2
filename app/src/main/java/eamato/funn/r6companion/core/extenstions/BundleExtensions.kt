package eamato.funn.r6companion.core.extenstions

import android.os.Build
import android.os.Bundle
import java.io.Serializable

fun Bundle.putSerializableList(list: List<Serializable>, key: String) {
    if (list.isEmpty()) {
        return
    }

    putInt("${key}_size", list.size)
    list.forEachIndexed { index, item -> putSerializable("${key}_${index}", item) }
}

@Suppress("UNCHECKED_CAST")
fun <T: Serializable> Bundle?.getSerializableList(key: String, clazz: Class<T>): List<T>? {
    if (this == null) {
        return null
    }

    return try {
        val listSize = getInt("${key}_size")
        val result = List(listSize) { index ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getSerializable("${key}_${index}", clazz)
            } else {
                getSerializable("${key}_${index}") as T
            }
        }
        result.filterNotNull()
    } catch (e: Exception) {
        e.printStackTrace()

        null
    }
}
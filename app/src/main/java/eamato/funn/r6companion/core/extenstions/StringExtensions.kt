package eamato.funn.r6companion.core.extenstions

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.gson.Gson

fun <T> String.fromJson(classOfT: Class<T>): T? {
    try {
        return Gson().fromJson(this, classOfT)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}

fun String.openUrlInBrowser(context: Context) {
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(this)))
}
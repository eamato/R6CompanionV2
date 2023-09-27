package eamato.funn.r6companion.core.extenstions

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun Context?.isCurrentlyConnectedToInternet(): Boolean {
    if (this == null)
        return false

    return ContextCompat.getSystemService(this, ConnectivityManager::class.java)?.isCurrentConnectedToInternet() ?: false
}

fun Context?.isCurrentlyConnectedNetworkWIFI(): Boolean {
    if (this == null)
        return false

    return ContextCompat.getSystemService(this, ConnectivityManager::class.java)?.isCurrentConnectedNetworkWIFI() ?: false
}

fun Context?.isLandscape(): Boolean {
    if (this == null)
        return false

    return this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

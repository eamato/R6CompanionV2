package eamato.funn.r6companion.core.extenstions

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun ConnectivityManager.isCurrentConnectedToInternet(): Boolean {
    val network = activeNetwork
    val networkCapabilities = getNetworkCapabilities(network) ?: return false

    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
}

fun ConnectivityManager.isCurrentConnectedNetworkWIFI(): Boolean {
    val network = activeNetwork
    val networkCapabilities = getNetworkCapabilities(network) ?: return false

    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
}
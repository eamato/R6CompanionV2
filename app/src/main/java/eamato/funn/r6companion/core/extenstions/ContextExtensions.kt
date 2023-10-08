package eamato.funn.r6companion.core.extenstions

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import androidx.annotation.XmlRes
import androidx.core.content.ContextCompat
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

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

fun Context.getDataFromXmlResource(@XmlRes xmlResId: Int, tag: String): List<String> {
    val tagsList = mutableListOf<String>()
    try {
        val xpp: XmlPullParser = resources.getXml(xmlResId)
        while (xpp.eventType != XmlPullParser.END_DOCUMENT) {
            if (xpp.eventType == XmlPullParser.START_TAG) {
                if (xpp.name == tag) {
                    tagsList.add(xpp.getAttributeValue(0))
                }
            }
            xpp.next()
        }
    } catch (e: XmlPullParserException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return tagsList.toList()
}

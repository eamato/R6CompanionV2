package eamato.funn.r6companion.core.okhttp

import android.content.Context
import android.util.Log
import eamato.funn.r6companion.core.CONNECTION_TIME_OUT
import eamato.funn.r6companion.core.IMAGE_LOGGER_TAG
import eamato.funn.r6companion.core.READ_TIME_OUT
import eamato.funn.r6companion.core.WRITE_TIME_OUT
import eamato.funn.r6companion.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

val defaultLoggingInterceptor = HttpLoggingInterceptor()
    .also {
        it.level = HttpLoggingInterceptor.Level.BODY
    }

val imageLoggingInterceptor = HttpLoggingInterceptor {
    Log.d(IMAGE_LOGGER_TAG, it)
}
    .also {
        it.level = HttpLoggingInterceptor.Level.BODY
    }

val requestInterceptor = Interceptor { chain ->
    val oldRequest = chain.request()

    val url = oldRequest
        .url
        .newBuilder()
        .build()

    val newRequest = oldRequest
        .newBuilder()
        .url(url)
        .build()

    chain.proceed(newRequest)
}

val defaultOkHttpClient = OkHttpClient
    .Builder()
    .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS)
    .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
    .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
    .addInterceptor(requestInterceptor)
    .also {
        if (BuildConfig.DEBUG) {
            it.addInterceptor(defaultLoggingInterceptor)
        }
    }
    .build()

fun getImageOkHttpClient(context: Context) = defaultOkHttpClient
    .newBuilder()
//    .addInterceptor(getWifiOnlyRequestInterceptor(context))
    .also {
        if (BuildConfig.DEBUG) {
            it.addInterceptor(imageLoggingInterceptor)
        }
    }
    .build()
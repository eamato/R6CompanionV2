package eamato.funn.r6companion.data.network

import eamato.funn.r6companion.core.NEWS_HOST
import eamato.funn.r6companion.core.okhttp.defaultOkHttpClient
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

    private val logger = DefaultAppLogger.getInstance()

    init {
        logger.i(Message.message {
            clazz = this@RetrofitService::class.java
            message = "Service initialized"
        })
    }

    val service: Retrofit = Retrofit.Builder()
        .baseUrl(NEWS_HOST)
        .client(defaultOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
package eamato.funn.r6companion.core.di

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eamato.funn.r6companion.BuildConfig
import eamato.funn.r6companion.core.CONNECTION_TIME_OUT
import eamato.funn.r6companion.core.IMAGE_LOGGER_TAG
import eamato.funn.r6companion.core.READ_TIME_OUT
import eamato.funn.r6companion.core.WRITE_TIME_OUT
import eamato.funn.r6companion.core.extenstions.isCurrentlyConnectedNetworkWIFI
import eamato.funn.r6companion.core.storage.PreferenceManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpModule {

    @Singleton
    @Provides
    @DefaultLoggingInterceptor
    fun provideDefaultLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }

    @Singleton
    @Provides
    @ImageLoggingInterceptor
    fun provideImageLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor { Log.d(IMAGE_LOGGER_TAG, it) }.also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    @DefaultRequestInterceptor
    fun provideDefaultRequestInterceptor(): Interceptor = Interceptor { chain ->
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

    @Singleton
    @Provides
    @ImageRequestInterceptor
    fun provideImageRequestInterceptor(
        @ApplicationContext context: Context,
        preferenceManager: PreferenceManager
    ): Interceptor = Interceptor { chain ->
        runBlocking {
            if (context.isCurrentlyConnectedNetworkWIFI()) {
                return@runBlocking chain.proceed(chain.request())
            }

            if (preferenceManager.useMobileNetworkForImages.firstOrNull() == true) {
                return@runBlocking chain.proceed(chain.request())
            }

            return@runBlocking Response.Builder()
                .code(400)
                .message("Network is not Wi-Fi")
                .protocol(Protocol.HTTP_1_0)
                .request(chain.request())
                .build()
        }
    }

    @Singleton
    @Provides
    @DefaultOkHttpClient
    fun provideDefaultOkHttpClient(
        @DefaultLoggingInterceptor defaultLoggingInterceptor: HttpLoggingInterceptor,
        @DefaultRequestInterceptor defaultRequestInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient
        .Builder()
        .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS)
        .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
        .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
        .addInterceptor(defaultRequestInterceptor)
        .also {
            if (BuildConfig.DEBUG) {
                it.addInterceptor(defaultLoggingInterceptor)
            }
        }
        .build()

    @Singleton
    @Provides
    @ImageOkHttpClient
    fun provideImageOkHttpClient(
        @ImageLoggingInterceptor imageLoggingInterceptor: HttpLoggingInterceptor,
        @ImageRequestInterceptor imageRequestInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient
        .Builder()
        .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS)
        .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
        .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
        .addInterceptor(imageRequestInterceptor)
        .also {
            if (BuildConfig.DEBUG) {
                it.addInterceptor(imageLoggingInterceptor)
            }
        }
        .build()
}
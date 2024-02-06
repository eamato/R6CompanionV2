package eamato.funn.r6companion.core.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eamato.funn.r6companion.core.AppInitializer
import eamato.funn.r6companion.core.UBISOFT_GRAPH_APP_ID_HEADER
import eamato.funn.r6companion.core.UBISOFT_GRAPH_APP_ID_HEADER_VALUE
import eamato.funn.r6companion.core.UBISOFT_GRAPH_APP_NAME_HEADER
import eamato.funn.r6companion.core.UBISOFT_GRAPH_APP_NAME_HEADER_VALUE
import eamato.funn.r6companion.core.UBISOFT_GRAPH_AUTHORIZATION_TOKEN_HEADER
import eamato.funn.r6companion.core.UBISOFT_GRAPH_AUTHORIZATION_TOKEN_HEADER_VALUE
import eamato.funn.r6companion.core.UBISOFT_GRAPH_URL
import eamato.funn.r6companion.core.storage.PreferenceManager
import eamato.funn.r6companion.core.storage.SavedOperatorsManager
import eamato.funn.r6companion.data.FirebaseRemoteConfigService
import eamato.funn.r6companion.data.network.NewsRetrofitService
import eamato.funn.r6companion.data.network.RetrofitService
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePreferenceManager(@ApplicationContext context: Context) = PreferenceManager(context)

    @Singleton
    @Provides
    fun provideSavedOperatorsManager(@ApplicationContext context: Context) = SavedOperatorsManager(context)

    @Singleton
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build()
        )
    }

    @Singleton
    @Provides
    fun provideRemoteConfigService(
        firebaseRemoteConfig: FirebaseRemoteConfig
    ) = FirebaseRemoteConfigService(firebaseRemoteConfig)

    @Singleton
    @Provides
    fun provideAppInitializer(
        @ApplicationContext context: Context,
        firebaseRemoteConfigService: FirebaseRemoteConfigService
    ) = AppInitializer(firebaseRemoteConfigService, context)

    @Singleton
    @Provides
    fun provideRetrofitService(
        @DefaultOkHttpClient defaultOkHttpClient: OkHttpClient
    ) = RetrofitService(defaultOkHttpClient)

    @Singleton
    @Provides
    fun provideNewsRetrofitService(
        retrofitService: RetrofitService
    ): NewsRetrofitService = retrofitService.service.create(NewsRetrofitService::class.java)

    @Singleton
    @Provides
    fun provideApolloClient(
        @DefaultOkHttpClient defaultOkHttpClient: OkHttpClient
    ): ApolloClient = ApolloClient.Builder()
        .serverUrl(UBISOFT_GRAPH_URL)
        .addHttpHeader(UBISOFT_GRAPH_APP_ID_HEADER, UBISOFT_GRAPH_APP_ID_HEADER_VALUE)
        .addHttpHeader(UBISOFT_GRAPH_AUTHORIZATION_TOKEN_HEADER, UBISOFT_GRAPH_AUTHORIZATION_TOKEN_HEADER_VALUE)
        .addHttpHeader(UBISOFT_GRAPH_APP_NAME_HEADER, UBISOFT_GRAPH_APP_NAME_HEADER_VALUE)
        .okHttpClient(defaultOkHttpClient)
        .build()
}
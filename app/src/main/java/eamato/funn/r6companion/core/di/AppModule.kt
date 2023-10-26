package eamato.funn.r6companion.core.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
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
import eamato.funn.r6companion.core.storage.PreferenceManager
import eamato.funn.r6companion.data.FirebaseRemoteConfigService
import eamato.funn.r6companion.data.network.NewsRetrofitService
import eamato.funn.r6companion.data.network.RetrofitService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providePreferenceManager(@ApplicationContext context: Context) = PreferenceManager(context)

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
    fun provideRetrofitService() = RetrofitService()

    @Singleton
    @Provides
    fun provideNewsRetrofitService(
        retrofitService: RetrofitService
    ): NewsRetrofitService = retrofitService.service.create(NewsRetrofitService::class.java)

    @Singleton
    @Provides
    fun provideApolloClient(): ApolloClient = ApolloClient.Builder().build()
}
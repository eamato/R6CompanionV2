package eamato.funn.r6companion.core.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eamato.funn.r6companion.data.FirebaseRemoteConfigService
import eamato.funn.r6companion.data.network.NewsRetrofitService
import eamato.funn.r6companion.data.sources.local.operators.ILocalDataSource
import eamato.funn.r6companion.data.sources.local.operators.LocalDataSourceAssets
import eamato.funn.r6companion.data.sources.local.operators.LocalDataSourceFile
import eamato.funn.r6companion.data.sources.remote.maps.RemoteDataSourceMaps
import eamato.funn.r6companion.data.sources.remote.news.RemoteDataSourceNews
import eamato.funn.r6companion.data.sources.remote.operators.RemoteDataSourceFirebaseRemoteConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourcesModule {

    @Singleton
    @Provides
    @LocalDataSourceAssetsQualifier
    fun provideAssetsDataSource(
        @ApplicationContext context: Context
    ): ILocalDataSource = LocalDataSourceAssets(context)

    @Singleton
    @Provides
    @LocalDataSourceFileQualifier
    fun provideLocalDataSource(
        @ApplicationContext context: Context
    ): ILocalDataSource = LocalDataSourceFile(context)

    @Singleton
    @Provides
    fun provideRemoteFirebaseDataSource(
        firebaseRemoteConfigService: FirebaseRemoteConfigService
    ): eamato.funn.r6companion.data.sources.remote.operators.IRemoteDataSource {
        return RemoteDataSourceFirebaseRemoteConfig(firebaseRemoteConfigService)
    }

    @Singleton
    @Provides
    fun provideNewsRemoteDataSource(
        newsRetrofitService: NewsRetrofitService
    ): eamato.funn.r6companion.data.sources.remote.news.IRemoteDataSource {
        return RemoteDataSourceNews(newsRetrofitService)
    }

    @Singleton
    @Provides
    fun provideMapsRemoteDataSource(
       apolloClient: ApolloClient
    ): eamato.funn.r6companion.data.sources.remote.maps.IRemoteDataSource {
        return RemoteDataSourceMaps(apolloClient)
    }
}
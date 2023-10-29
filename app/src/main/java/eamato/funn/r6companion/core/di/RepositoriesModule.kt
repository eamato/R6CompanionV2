package eamato.funn.r6companion.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eamato.funn.r6companion.data.repositories.maps.IMapsRepository
import eamato.funn.r6companion.data.repositories.maps.MapsRepository
import eamato.funn.r6companion.data.repositories.news.INewsRepository
import eamato.funn.r6companion.data.repositories.news.NewsRepository
import eamato.funn.r6companion.data.repositories.operators.IOperatorsRepository
import eamato.funn.r6companion.data.repositories.operators.OperatorsRepository
import eamato.funn.r6companion.data.sources.local.operators.ILocalDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Singleton
    @Provides
    fun provideOperatorsRepository(
        @ApplicationContext context: Context,
        @LocalDataSourceFileQualifier localDataSource: ILocalDataSource,
        remoteDataSource: eamato.funn.r6companion.data.sources.remote.operators.IRemoteDataSource,
        @LocalDataSourceAssetsQualifier lastChanceLocalDataSource: ILocalDataSource
    ): IOperatorsRepository = OperatorsRepository(
        context,
        localDataSource,
        remoteDataSource,
        lastChanceLocalDataSource
    )

    @Singleton
    @Provides
    fun provideNewsRepository(
        @ApplicationContext context: Context,
        remoteDataSource: eamato.funn.r6companion.data.sources.remote.news.IRemoteDataSource
    ): INewsRepository = NewsRepository(context, remoteDataSource)

    @Singleton
    @Provides
    fun provideMapsRepository(
        remoteDataSource: eamato.funn.r6companion.data.sources.remote.maps.IRemoteDataSource
    ): IMapsRepository = MapsRepository(remoteDataSource)
}
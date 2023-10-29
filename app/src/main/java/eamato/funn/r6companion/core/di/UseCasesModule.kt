package eamato.funn.r6companion.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eamato.funn.r6companion.data.repositories.maps.IMapsRepository
import eamato.funn.r6companion.data.repositories.news.INewsRepository
import eamato.funn.r6companion.data.repositories.operators.IOperatorsRepository
import eamato.funn.r6companion.domain.usecases.MapsUseCase
import eamato.funn.r6companion.domain.usecases.NewsUseCase
import eamato.funn.r6companion.domain.usecases.OperatorsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Singleton
    @Provides
    fun provideOperatorsUseCase(
        operatorsRepository: IOperatorsRepository
    ) = OperatorsUseCase(operatorsRepository)

    @Singleton
    @Provides
    fun provideNewsUseCase(
        newsRepository: INewsRepository
    ) = NewsUseCase(newsRepository)

    @Singleton
    @Provides
    fun provideMapsUseCase(
        mapsRepository: IMapsRepository
    ) = MapsUseCase(mapsRepository)
}
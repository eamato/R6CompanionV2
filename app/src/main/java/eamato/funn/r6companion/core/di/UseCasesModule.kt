package eamato.funn.r6companion.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eamato.funn.r6companion.data.repositories.about.IAboutRepository
import eamato.funn.r6companion.data.repositories.maps.IMapsRepository
import eamato.funn.r6companion.data.repositories.news.INewsRepository
import eamato.funn.r6companion.data.repositories.operators.IOperatorsRepository
import eamato.funn.r6companion.data.repositories.weapons.IWeaponsRepository
import eamato.funn.r6companion.domain.usecases.AboutUseCase
import eamato.funn.r6companion.domain.usecases.MapDetailsUseCase
import eamato.funn.r6companion.domain.usecases.MapsListUseCase
import eamato.funn.r6companion.domain.usecases.NewsUseCase
import eamato.funn.r6companion.domain.usecases.OperatorByIdUseCase
import eamato.funn.r6companion.domain.usecases.OperatorsUseCase
import eamato.funn.r6companion.domain.usecases.WeaponsUseCase
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
    fun provideOperatorByIdUseCase(
        operatorsRepository: IOperatorsRepository
    ) = OperatorByIdUseCase(operatorsRepository)

    @Singleton
    @Provides
    fun provideNewsUseCase(
        newsRepository: INewsRepository
    ) = NewsUseCase(newsRepository)

    @Singleton
    @Provides
    fun provideMapsUseCase(
        mapsRepository: IMapsRepository
    ) = MapsListUseCase(mapsRepository)

    @Singleton
    @Provides
    fun provideMapDetailsUseCase(
        mapsRepository: IMapsRepository
    ) = MapDetailsUseCase(mapsRepository)

    @Singleton
    @Provides
    fun provideAboutUseCase(
        aboutRepository: IAboutRepository
    ) = AboutUseCase(aboutRepository)

    @Singleton
    @Provides
    fun provideWeaponsUseCase(
        weaponsRepository: IWeaponsRepository
    ) = WeaponsUseCase(weaponsRepository)
}
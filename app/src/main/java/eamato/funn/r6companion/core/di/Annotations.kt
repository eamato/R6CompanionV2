package eamato.funn.r6companion.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalDataSourceAssetsQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalDataSourceFileQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultLoggingInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ImageLoggingInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultRequestInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ImageRequestInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ImageOkHttpClient
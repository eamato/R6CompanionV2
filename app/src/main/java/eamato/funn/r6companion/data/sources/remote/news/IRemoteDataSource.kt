package eamato.funn.r6companion.data.sources.remote.news

import eamato.funn.r6companion.data.entities.NewsRequestParams
import eamato.funn.r6companion.data.entities.Updates

interface IRemoteDataSource {

    suspend fun getNews(newsRequestParams: NewsRequestParams): Updates
}
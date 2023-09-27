package eamato.funn.r6companion.data.repositories.news

import eamato.funn.r6companion.data.entities.NewsRequestParams
import eamato.funn.r6companion.data.entities.Updates

interface INewsRepository {

    suspend fun getNews(newsRequestParams: NewsRequestParams): Updates
}
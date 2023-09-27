package eamato.funn.r6companion.data.sources.remote.news

import eamato.funn.r6companion.data.entities.NewsRequestParams
import eamato.funn.r6companion.data.entities.Updates
import eamato.funn.r6companion.data.network.NewsRetrofitService
import javax.inject.Inject

class RemoteDataSourceNews @Inject constructor(
    private val newsRetrofitService: NewsRetrofitService
) : IRemoteDataSource {

    override suspend fun getNews(newsRequestParams: NewsRequestParams): Updates {
        return newsRetrofitService.getUpdatesCoroutines(
            skip = newsRequestParams.skip,
            newsCount = newsRequestParams.newsCount,
            newsLocale = newsRequestParams.newsLocale,
            newsTag = newsRequestParams.newsTag,
            newsCategoriesFilter = newsRequestParams.newsCategoriesFilter
        )
    }
}
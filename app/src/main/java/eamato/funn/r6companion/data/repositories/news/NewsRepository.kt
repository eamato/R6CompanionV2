package eamato.funn.r6companion.data.repositories.news

import android.content.Context
import eamato.funn.r6companion.data.entities.NewsRequestParams
import eamato.funn.r6companion.data.entities.Updates
import eamato.funn.r6companion.data.sources.remote.news.IRemoteDataSource
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val context: Context,
    private val remoteDataSource: IRemoteDataSource
) : INewsRepository {

    override suspend fun getNews(newsRequestParams: NewsRequestParams): Updates {
        return remoteDataSource.getNews(newsRequestParams)
    }
}
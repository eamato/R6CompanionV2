package eamato.funn.r6companion.domain.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import eamato.funn.r6companion.core.AD_INSERTION_COUNT
import eamato.funn.r6companion.core.DEFAULT_NEWS_LOCALE
import eamato.funn.r6companion.core.extenstions.insertItemAtEveryStep
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import eamato.funn.r6companion.data.entities.NewsRequestParams
import eamato.funn.r6companion.data.entities.Updates
import eamato.funn.r6companion.data.repositories.news.INewsRepository
import eamato.funn.r6companion.domain.entities.news.AdvertisedNewsArticle
import eamato.funn.r6companion.domain.mappers.news.NewsArticleUseCaseMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.toImmutableList

class NewsPagingSource constructor(
    private val newsRepository: INewsRepository,
    private val newsLocale: String = DEFAULT_NEWS_LOCALE,
    private val newsCategory: String? = null
) : PagingSource<Int, AdvertisedNewsArticle>() {

    private val _newsCategoryValue = MutableLiveData<String?>(null)
    val newsCategoryValue: LiveData<String?> = _newsCategoryValue

    private val logger = DefaultAppLogger.getInstance()

    private val newsMapper = NewsArticleUseCaseMapper

    override fun getRefreshKey(state: PagingState<Int, AdvertisedNewsArticle>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null

        val refreshKey =
            page.prevKey?.plus(state.config.pageSize) ?: page.nextKey?.minus(state.config.pageSize)

        logger.d(Message.message {
            clazz = this@NewsPagingSource::class.java
            message = "getRefreshKey refreshKey = $refreshKey"
        })

        return refreshKey
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AdvertisedNewsArticle> {
        val skip = params.key ?: 0
        val newsCount = params.loadSize

        val newsRequestParams = NewsRequestParams(
            skip = skip,
            newsLocale = newsLocale,
            newsCategoriesFilter = newsCategory,
            newsCount = newsCount
        )

        try {
            val response = newsRepository.getNews(newsRequestParams)
            val updates = response.items?.filterNotNull() ?: emptyList()
            val result = mapToAdvertisedArticle(updates)

            if (skip == 0) {
                _newsCategoryValue.value = response.categoriesFilter
            }

            val prevKey = if (skip == 0) {
                null
            } else {
                skip - newsCount
            }

            val nextKey = if (updates.isEmpty()) {
                null
            } else {
                skip + newsCount
            }

            return LoadResult.Page(result, prevKey, nextKey)
        } catch (e: Exception) {
            logger.e(msg = Message.message {
                clazz = this@NewsPagingSource::class.java
                message = "load error"
            }, err = e)

            return LoadResult.Error(e)
        }
    }

    private suspend fun mapToAdvertisedArticle(
        updates: List<Updates.Item?>
    ) = withContext(Dispatchers.IO) {
        updates
            .filterNotNull()
            .mapNotNull { update ->
                newsMapper.map(update)?.let { newsArticle ->
                    AdvertisedNewsArticle(newsArticle)
                }
            }
            .let { advertisedArticles ->
                advertisedArticles
                    .takeIf { it.size >= AD_INSERTION_COUNT }
                    ?.toMutableList()
                    ?.also { mutableAdvertisedArticles ->
                        mutableAdvertisedArticles.insertItemAtEveryStep(
                            AdvertisedNewsArticle(null, true),
                            AD_INSERTION_COUNT
                        )
                    }
                    ?.toImmutableList()
                    ?: advertisedArticles
            }
    }
}
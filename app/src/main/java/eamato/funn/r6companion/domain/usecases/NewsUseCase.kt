package eamato.funn.r6companion.domain.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import eamato.funn.r6companion.core.DEFAULT_NEWS_LOCALE
import eamato.funn.r6companion.core.NEWS_COUNT_DEFAULT_VALUE
import eamato.funn.r6companion.core.NEWS_MAX_PAGE_SIZE
import eamato.funn.r6companion.core.NEWS_PREFETCH_DISTANCE
import eamato.funn.r6companion.core.utils.OneSourceMediatorLiveData
import eamato.funn.r6companion.data.repositories.news.INewsRepository
import eamato.funn.r6companion.domain.paging.NewsPagingSource
import javax.inject.Inject

class NewsUseCase @Inject constructor(
    private val newsRepository: INewsRepository
) {

    private val pagingConfig = PagingConfig(
        pageSize = NEWS_COUNT_DEFAULT_VALUE,
        prefetchDistance = NEWS_PREFETCH_DISTANCE,
        enablePlaceholders = true,
        initialLoadSize = NEWS_COUNT_DEFAULT_VALUE,
        maxSize = NEWS_MAX_PAGE_SIZE
    )

    operator fun invoke(
        newsLocale: String = DEFAULT_NEWS_LOCALE,
        newsCategory: String? = null,
        categoryValue: OneSourceMediatorLiveData<String?>
    ) = Pager(
        config = pagingConfig,
        pagingSourceFactory = {
            NewsPagingSource(newsRepository, newsLocale, newsCategory)
                .also {
                    categoryValue.setSource(it.newsCategoryValue) { data ->
                        categoryValue.value = data
                    }
                }
        }
    )
}
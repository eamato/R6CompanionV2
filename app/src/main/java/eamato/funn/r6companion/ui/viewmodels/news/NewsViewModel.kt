package eamato.funn.r6companion.ui.viewmodels.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import eamato.funn.r6companion.core.DEFAULT_NEWS_LOCALE
import eamato.funn.r6companion.core.utils.OneSourceMediatorLiveData
import eamato.funn.r6companion.domain.entities.news.AdvertisedNewsArticle
import eamato.funn.r6companion.domain.usecases.NewsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsUseCase: NewsUseCase
) : ViewModel() {

    private val _news = MutableLiveData<PagingData<AdvertisedNewsArticle>>()
    val news: LiveData<PagingData<AdvertisedNewsArticle>> = _news

    private val _newsCategoryValue = OneSourceMediatorLiveData<String?>()
    val newsCategoryValue: LiveData<String?> = _newsCategoryValue

    private var job: Job? = null

    private var currentNewsLocale: String? = null
    private var currentNewsCategory: String? = null

    init {
        val newsStream = getNews()
        if (newsStream != null) {
            job = viewModelScope.launch {
                newsStream.collect { pagingData ->
                    _news.value = pagingData
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        job?.cancel()
        job = null
    }

    fun refreshNews(newsLocale: String = DEFAULT_NEWS_LOCALE, newsCategory: String? = null) {
        val newsStream = getNews(newsLocale, newsCategory) ?: return

        job?.cancel()
        job = viewModelScope.launch {
            _news.value = PagingData.empty()
            newsStream.collect { pagingData ->
                _news.value = pagingData
            }
        }
    }

    private fun getNews(
        newsLocale: String = DEFAULT_NEWS_LOCALE,
        newsCategory: String? = null
    ): Flow<PagingData<AdvertisedNewsArticle>>? {
        if (currentNewsLocale == newsLocale && currentNewsCategory == newsCategory) {
            return null
        }

        currentNewsLocale = newsLocale
        currentNewsCategory = newsCategory

        return newsUseCase(newsLocale, newsCategory, _newsCategoryValue)
            .flow
            .cachedIn(viewModelScope)
    }
}
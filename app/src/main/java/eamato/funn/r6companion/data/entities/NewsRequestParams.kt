package eamato.funn.r6companion.data.entities

import eamato.funn.r6companion.core.DEFAULT_NEWS_LOCALE
import eamato.funn.r6companion.core.NEWS_COUNT_DEFAULT_VALUE
import eamato.funn.r6companion.core.NEWS_TAG_PARAM_R6_VALUE

data class NewsRequestParams(
    val skip: Int = 0,
    val newsCount: Int = NEWS_COUNT_DEFAULT_VALUE,
    val newsLocale: String = DEFAULT_NEWS_LOCALE,
    val newsTag: String = NEWS_TAG_PARAM_R6_VALUE,
    @NewsCategory.Companion.NewsCategory val newsCategoriesFilter: String? = null
)

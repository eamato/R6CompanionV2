package eamato.funn.r6companion.data.network

import eamato.funn.r6companion.core.DEFAULT_NEWS_LOCALE
import eamato.funn.r6companion.core.NEWS_AUTHORIZATION_TOKEN_HEADER
import eamato.funn.r6companion.core.NEWS_CATEGORIES_FILTER_PARAM_KEY
import eamato.funn.r6companion.core.NEWS_COUNT_DEFAULT_VALUE
import eamato.funn.r6companion.core.NEWS_COUNT_PARAM_KEY
import eamato.funn.r6companion.core.NEWS_LOCALE_PARAM_KEY
import eamato.funn.r6companion.core.NEWS_PATH
import eamato.funn.r6companion.core.NEWS_SKIP_PARAM_KEY
import eamato.funn.r6companion.core.NEWS_TAG_PARAM_KEY
import eamato.funn.r6companion.core.NEWS_TAG_PARAM_R6_VALUE
import eamato.funn.r6companion.data.entities.Updates
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsRetrofitService {

    @Headers(NEWS_AUTHORIZATION_TOKEN_HEADER)
    @GET(NEWS_PATH)
    suspend fun getUpdatesCoroutines(
        @Query(NEWS_SKIP_PARAM_KEY, encoded = true) skip: Int = 0,
        @Query(NEWS_COUNT_PARAM_KEY, encoded = true) newsCount: Int = NEWS_COUNT_DEFAULT_VALUE,
        @Query(NEWS_LOCALE_PARAM_KEY, encoded = true) newsLocale: String = DEFAULT_NEWS_LOCALE,
        @Query(NEWS_TAG_PARAM_KEY, encoded = true) newsTag: String = NEWS_TAG_PARAM_R6_VALUE,
        @Query(NEWS_CATEGORIES_FILTER_PARAM_KEY, encoded = true) newsCategoriesFilter: String? = null
    ): Updates
}
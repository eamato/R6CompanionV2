package eamato.funn.r6companion.data.entities

import androidx.annotation.StringDef

class NewsCategory {

    companion object {
        @StringDef(
            NEWS_CATEGORIES_FILTER_PARAM_ALL_VALUE,
            NEWS_CATEGORIES_FILTER_PARAM_ESPORTS_VALUE,
            NEWS_CATEGORIES_FILTER_PARAM_GAME_UPDATES_VALUE,
            NEWS_CATEGORIES_FILTER_PARAM_COMMUNITY_VALUE,
            NEWS_CATEGORIES_FILTER_PARAM_PATCH_NOTES_VALUE,
            NEWS_CATEGORIES_FILTER_PARAM_STORE_VALUE
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class NewsCategory

        const val NEWS_CATEGORIES_FILTER_PARAM_ALL_VALUE = "all"
        const val NEWS_CATEGORIES_FILTER_PARAM_ESPORTS_VALUE = "esports"
        const val NEWS_CATEGORIES_FILTER_PARAM_GAME_UPDATES_VALUE = "game-updates"
        const val NEWS_CATEGORIES_FILTER_PARAM_COMMUNITY_VALUE = "community"
        const val NEWS_CATEGORIES_FILTER_PARAM_PATCH_NOTES_VALUE = "patch-notes"
        const val NEWS_CATEGORIES_FILTER_PARAM_STORE_VALUE = "store"
    }
}
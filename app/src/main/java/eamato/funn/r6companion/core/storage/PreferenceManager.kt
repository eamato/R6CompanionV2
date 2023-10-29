package eamato.funn.r6companion.core.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.DEFAULT_NEWS_LOCALE
import eamato.funn.r6companion.core.PREFERENCE_KEY_USER_SETTINGS
import eamato.funn.r6companion.core.extenstions.getDataFromXmlResource
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceManager @Inject constructor(private val context: Context) {

    private companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            PREFERENCE_KEY_USER_SETTINGS
        )

        val USER_SETTINGS_KEY_NEWS_LOCALE = stringPreferencesKey("news_locale")
        val USER_SETTINGS_IS_SAME_LOCALE = booleanPreferencesKey("is_same_locale")
        val USER_SETTINGS_USE_MOBILE_NETWORK_FOR_IMAGES =
            booleanPreferencesKey("use_mobile_network_for_images")
    }

    val newsLocale = context.dataStore.data
        .map { preferences -> preferences[USER_SETTINGS_KEY_NEWS_LOCALE] ?: DEFAULT_NEWS_LOCALE }

    val isSameLocale = context.dataStore.data
        .map { preferences -> preferences[USER_SETTINGS_IS_SAME_LOCALE] ?: false }

    val useMobileNetworkForImages = context.dataStore.data
        .map { preferences -> preferences[USER_SETTINGS_USE_MOBILE_NETWORK_FOR_IMAGES] ?: true }

    suspend fun changeNewsLocale(newsLocale: String) {
        context.dataStore
            .edit { preferences -> preferences[USER_SETTINGS_KEY_NEWS_LOCALE] = newsLocale }
    }

    suspend fun changeUseSaveLanguageForAppAndNews(isSame: Boolean) {
        context.dataStore
            .edit { preferences -> preferences[USER_SETTINGS_IS_SAME_LOCALE] = isSame }
    }

    suspend fun changeUseMobileNetworkForImages(useMobileNetworkForImages: Boolean) {
        context.dataStore
            .edit { preferences ->
                preferences[USER_SETTINGS_USE_MOBILE_NETWORK_FOR_IMAGES] = useMobileNetworkForImages
            }
    }

    fun getListOfSupportedLocales(): List<String> =
        context.getDataFromXmlResource(R.xml.locale_config, "locale")
}
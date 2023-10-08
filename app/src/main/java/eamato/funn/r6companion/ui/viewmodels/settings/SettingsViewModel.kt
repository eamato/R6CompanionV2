package eamato.funn.r6companion.ui.viewmodels.settings

import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.DEFAULT_NEWS_LOCALE
import eamato.funn.r6companion.core.NEWS_LOCALES
import eamato.funn.r6companion.core.extenstions.localeTagToLocaleDisplayName
import eamato.funn.r6companion.core.storage.PreferenceManager
import eamato.funn.r6companion.core.utils.SingleLiveEvent
import eamato.funn.r6companion.core.utils.UiText
import eamato.funn.r6companion.ui.entities.PopupContentItem
import eamato.funn.r6companion.ui.entities.settings.SettingsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _settingsChangedEvent = SingleLiveEvent<Unit>()
    val settingsChangedEvent: LiveData<Unit> = _settingsChangedEvent

    private val _settingsItems = MutableLiveData<List<SettingsItem>>(emptyList())
    val settingsItems: LiveData<List<SettingsItem>> = _settingsItems

    init {
        viewModelScope.launch {
            preferenceManager.newsLocale.distinctUntilChanged().collectLatest {
                _settingsChangedEvent.value = Unit
            }
        }

        viewModelScope.launch {
            preferenceManager.isSameLocale.distinctUntilChanged().collectLatest {
                _settingsChangedEvent.value = Unit
            }
        }
    }

    fun initSettingsList() {
        viewModelScope.launch {
            _settingsItems.value = createListOfSettingsItems()
        }
    }

    private suspend fun createListOfSettingsItems(): List<SettingsItem> =
        withContext(Dispatchers.IO) {
            val settingsItems = mutableListOf<SettingsItem>()

            val currentNewsLocale =
                preferenceManager.newsLocale.firstOrNull() ?: DEFAULT_NEWS_LOCALE

            val currentIsSameLocale = preferenceManager.isSameLocale.firstOrNull() ?: false

            val currentUseMobileNetworkForImages =
                preferenceManager.useMobileNetworkForImages.firstOrNull() ?: true

            val currentAppLocale = getCurrentAppLocale()

            val listOfSupportedAppLocales = preferenceManager.getListOfSupportedLocales()

            val supportedAppLocales = listOfSupportedAppLocales
                .associateWith { localeTag -> localeTag.localeTagToLocaleDisplayName() }

            val supportedNewsLocales = NEWS_LOCALES
                .associateWith { newsLocaleTag -> newsLocaleTag.localeTagToLocaleDisplayName() }

            val appLocalesToNewsLocales = listOfSupportedAppLocales
                .associateWith { localeTag ->
                    NEWS_LOCALES
                        .find { newsLocale ->
                            newsLocale.split("-").getOrNull(0) == localeTag
                        }
                        ?: DEFAULT_NEWS_LOCALE
                }

            settingsItems.add(
                SettingsItem.SettingsItemPopup(
                    icon = R.drawable.ic_companion_white_24dp,
                    title = R.string.settings_item_title_app_language,
                    subTitle = UiText.SimpleString(currentAppLocale.second),
                    isEnabled = true,
                    popupContentItems = supportedAppLocales.map { (key, value) ->
                        PopupContentItem(
                            icon = null,
                            title = UiText.SimpleString(value),
                            subTitle = null,
                            onClickListener = {
                                assert(Looper.myLooper() == Looper.getMainLooper())
                                val appLocales = LocaleListCompat.forLanguageTags(key)
                                AppCompatDelegate.setApplicationLocales(appLocales)

                                if (currentIsSameLocale) {
                                    updateNewsLocale(
                                        appLocalesToNewsLocales.getOrDefault(
                                            key,
                                            DEFAULT_NEWS_LOCALE
                                        )
                                    )
                                }

                                _settingsChangedEvent.value = Unit
                            }
                        )
                    }
                )
            )

            settingsItems.add(
                SettingsItem.SettingsItemPopup(
                    icon = R.drawable.ic_companion_white_24dp,
                    title = R.string.settings_item_title_news_locale,
                    subTitle = UiText.SimpleString(currentNewsLocale.localeTagToLocaleDisplayName()),
                    isEnabled = !currentIsSameLocale,
                    popupContentItems = supportedNewsLocales.map { (key, value) ->
                        PopupContentItem(
                            icon = null,
                            title = UiText.SimpleString(value),
                            subTitle = null,
                            onClickListener = {
                                assert(Looper.myLooper() == Looper.getMainLooper())
                                updateNewsLocale(key)
                                _settingsChangedEvent.value = Unit
                            }
                        )
                    }
                )
            )

            settingsItems.add(
                SettingsItem.SettingsItemSwitch(
                    icon = R.drawable.ic_companion_white_24dp,
                    title = R.string.settings_item_title_use_same_language,
                    subTitle = null,
                    isChecked = currentIsSameLocale,
                    isEnabled = true,
                    onCheckedListener = { isChecked ->
                        viewModelScope.launch {
                            if (isChecked) {
                                updateNewsLocale(
                                    appLocalesToNewsLocales.getOrDefault(
                                        currentAppLocale.first,
                                        DEFAULT_NEWS_LOCALE
                                    )
                                )
                            }
                            preferenceManager.changeUseSaveLanguageForAppAndNews(isChecked)
                        }
                    }
                )
            )

            settingsItems.add(
                SettingsItem.SettingsItemSwitch(
                    icon = R.drawable.ic_companion_white_24dp,
                    title = R.string.settings_item_title_use_mobile_network_to_load_images,
                    subTitle = UiText.ResourceString(R.string.settings_item_subtitle_use_mobile_network_to_load_images),
                    isChecked = currentUseMobileNetworkForImages,
                    isEnabled = true,
                    onCheckedListener = { isChecked ->
                        viewModelScope.launch {
                            preferenceManager.changeUseMobileNetworkForImages(isChecked)
                        }
                    }
                )
            )

            settingsItems.add(
                SettingsItem.SettingsItemScreen(
                    icon = R.drawable.ic_companion_white_24dp,
                    title = R.string.settings_item_title_about
                )
            )

            settingsItems.toList()
        }

    private fun updateNewsLocale(newsLocale: String) {
        viewModelScope.launch {
            preferenceManager.changeNewsLocale(newsLocale)
        }
    }

    private fun getCurrentAppLocale(): Pair<String, String> {
        val locales = AppCompatDelegate.getApplicationLocales()
        if (locales.isEmpty) {
            val currentLocale = Locale.getDefault()
            return currentLocale.toLanguageTag() to
                    currentLocale.getDisplayName(currentLocale)
                        .replaceFirstChar { char -> char.uppercase(currentLocale) }
        }
        val firstLocale = locales[0] ?: kotlin.run {
            val currentLocale = Locale.getDefault()
            return currentLocale.toLanguageTag() to
                    currentLocale.getDisplayName(currentLocale)
                        .replaceFirstChar { char -> char.uppercase(currentLocale) }
        }

        return firstLocale.toLanguageTag() to
                firstLocale.getDisplayName(firstLocale)
                    .replaceFirstChar { char -> char.uppercase(firstLocale) }
    }
}
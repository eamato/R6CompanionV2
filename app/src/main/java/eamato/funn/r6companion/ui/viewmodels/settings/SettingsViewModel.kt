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
import eamato.funn.r6companion.core.SETTINGS_ITEM_ABOUT_SCREEN_ID
import eamato.funn.r6companion.core.SETTINGS_ITEM_APP_LANGUAGE_ID
import eamato.funn.r6companion.core.SETTINGS_ITEM_NEWS_LANGUAGE_ID
import eamato.funn.r6companion.core.SETTINGS_ITEM_SAME_LANGUAGE_ID
import eamato.funn.r6companion.core.SETTINGS_ITEM_USE_MOBILE_NETWORK_ID
import eamato.funn.r6companion.core.extenstions.localeTagToLocaleDisplayName
import eamato.funn.r6companion.core.storage.PreferenceManager
import eamato.funn.r6companion.core.utils.SelectableObject
import eamato.funn.r6companion.core.utils.SingleLiveEvent
import eamato.funn.r6companion.core.utils.UiText
import eamato.funn.r6companion.ui.entities.PopupContentItem
import eamato.funn.r6companion.ui.entities.settings.SettingsItem
import eamato.funn.r6companion.ui.fragments.settings.FragmentSettingsAbout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private val _settingsItems = MutableLiveData<List<SelectableObject<SettingsItem>>>(emptyList())
    val settingsItems: LiveData<List<SelectableObject<SettingsItem>>> = _settingsItems

    private val _clearContainerEvent = SingleLiveEvent<Unit>()
    val clearContainerEvent: LiveData<Unit> = _clearContainerEvent

    private val _showContentForSettingsItem = SingleLiveEvent<SettingsItem>()
    val showContentForSettingsItem: LiveData<SettingsItem> = _showContentForSettingsItem

    private var selectedSettingsItem: SettingsItem? = null

    private var settingsItemsCreatingJob: Job? = null

    init {
        viewModelScope.launch {
            preferenceManager
                .newsLocale
                .distinctUntilChanged()
                .collectLatest { initSettingsList() }
        }

        viewModelScope.launch {
            preferenceManager
                .isSameLocale
                .distinctUntilChanged()
                .collectLatest { initSettingsList() }
        }
    }

    override fun onCleared() {
        super.onCleared()

        settingsItemsCreatingJob?.cancel()
        settingsItemsCreatingJob = null
    }

    fun initSettingsList() {
        settingsItemsCreatingJob?.cancel()
        settingsItemsCreatingJob = viewModelScope.launch {
            val selectedSettingsItem = this@SettingsViewModel.selectedSettingsItem
            val settingsItems = createListOfSettingsItems()
                .map { settingsItem ->
                    val isSelected: Boolean = if (selectedSettingsItem != null) {
                        if (selectedSettingsItem.id == settingsItem.id) {
                            settingsItem.isEnabled
                        } else {
                            false
                        }
                    } else {
                        false
                    }

                    if (isSelected) {
                        setSelectedSettingsItem(settingsItem, false)
                    }

                    SelectableObject(settingsItem, isSelected)
                }

            if (settingsItems.find { settingsItem -> settingsItem.isSelected } == null) {
                settingsItems.firstOrNull()?.run {
                    _clearContainerEvent.value = Unit
                    val settingsItem = this.data
                    _showContentForSettingsItem.value = settingsItem
                    setSelectedSettingsItem(settingsItem, false)
                    isSelected = true
                }
            }

            _settingsItems.value = settingsItems
        }
    }

    fun setSelectedSettingsItem(
        selectedSettingsItem: SettingsItem?,
        shouldReInitSettingsItems: Boolean = true
    ) {
        if (selectedSettingsItem == null) {
            this.selectedSettingsItem = null
            return
        }

        if (!selectedSettingsItem.isSelectable) {
            return
        }

        if (this.selectedSettingsItem?.id != selectedSettingsItem.id) {
            this.selectedSettingsItem = selectedSettingsItem
            if (shouldReInitSettingsItems) {
                initSettingsList()
            }
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
                    id = SETTINGS_ITEM_APP_LANGUAGE_ID,
                    icon = R.drawable.ic_translate_24,
                    title = R.string.settings_item_title_app_language,
                    subTitle = UiText.SimpleString(currentAppLocale.second),
                    isEnabled = true,
                    popupContentItems = supportedAppLocales.map { (key, value) ->
                        PopupContentItem(
                            icon = null,
                            title = UiText.SimpleString(value),
                            subTitle = null,
                            onClickListener = {
                                if (key == currentAppLocale.first) {
                                    return@PopupContentItem
                                }

                                updateAppLocale(key)

                                if (currentIsSameLocale) {
                                    updateNewsLocale(
                                        appLocalesToNewsLocales.getOrDefault(
                                            key,
                                            DEFAULT_NEWS_LOCALE
                                        )
                                    )
                                }
                            }
                        )
                    }
                )
            )

            settingsItems.add(
                SettingsItem.SettingsItemPopup(
                    id = SETTINGS_ITEM_NEWS_LANGUAGE_ID,
                    icon = R.drawable.ic_translate_24,
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
                            }
                        )
                    }
                )
            )

            settingsItems.add(
                SettingsItem.SettingsItemSwitch(
                    id = SETTINGS_ITEM_SAME_LANGUAGE_ID,
                    icon = R.drawable.ic_language_24,
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
                    id = SETTINGS_ITEM_USE_MOBILE_NETWORK_ID,
                    icon = R.drawable.ic_mobile_network_24,
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
                    id = SETTINGS_ITEM_ABOUT_SCREEN_ID,
                    icon = R.drawable.ic_companion_white_24dp,
                    title = R.string.settings_item_title_about,
                    destinationId = R.id.FragmentSettingsAbout,
                    destinationClass = FragmentSettingsAbout::class,
                    args = null
                )
            )

            settingsItems.toList()
        }

    private fun updateNewsLocale(newsLocale: String) {
        viewModelScope.launch { preferenceManager.changeNewsLocale(newsLocale) }
    }

    private fun updateAppLocale(appLocale: String) {
        assert(Looper.myLooper() == Looper.getMainLooper())
        val appLocales = LocaleListCompat.forLanguageTags(appLocale)
        AppCompatDelegate.setApplicationLocales(appLocales)
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
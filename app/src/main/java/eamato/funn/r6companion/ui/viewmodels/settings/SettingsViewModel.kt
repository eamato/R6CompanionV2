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
import eamato.funn.r6companion.core.extenstions.areLocalesEqual
import eamato.funn.r6companion.core.extenstions.localeTagToLocaleDisplayName
import eamato.funn.r6companion.core.storage.PreferenceManager
import eamato.funn.r6companion.core.utils.SelectableObject
import eamato.funn.r6companion.core.utils.SingleLiveEvent
import eamato.funn.r6companion.core.utils.UiText
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import eamato.funn.r6companion.ui.entities.PopupContentItem
import eamato.funn.r6companion.ui.entities.settings.SettingsItem
import eamato.funn.r6companion.ui.fragments.settings.FragmentSettingsAbout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
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

    private var settingsItemsList: List<SettingsItem> = emptyList()

    private val listOfSupportedAppLocales = preferenceManager.getListOfSupportedLocales()

    private var selectedSettingsItem: SettingsItem? = null

    private val supportedAppLocales = listOfSupportedAppLocales
        .associateWith { localeTag -> localeTag.localeTagToLocaleDisplayName() }

    private val supportedNewsLocales = NEWS_LOCALES
        .associateWith { newsLocaleTag -> newsLocaleTag.localeTagToLocaleDisplayName() }

    private val appLocalesToNewsLocales = listOfSupportedAppLocales
        .associateWith { localeTag ->
            NEWS_LOCALES
                .find { newsLocale ->
                    newsLocale.split("-").getOrNull(0) == localeTag
                }
                ?: DEFAULT_NEWS_LOCALE
        }

    private var settingsItemsCreatingJob: Job? = null

    init {
        createSettingsItemsAsync()

        viewModelScope.launch {
            preferenceManager
                .newsLocale
                .drop(1)
                .distinctUntilChanged()
                .collectLatest {
                    createSettingsItemsAsync()
                }
        }

        viewModelScope.launch {
            preferenceManager
                .isSameLocale
                .drop(1)
                .distinctUntilChanged()
                .collectLatest {
                    createSettingsItemsAsync()
                }
        }
    }

    fun setSelectedSettingsItem(selectedSettingsItem: SettingsItem?) {
        if (selectedSettingsItem == null) {
            this.selectedSettingsItem = null
            _settingsItems.value = mapSettingsItemsToSelectableObject()
            return
        }

        if (!selectedSettingsItem.isSelectable) {
            return
        }

        if (this.selectedSettingsItem?.id != selectedSettingsItem.id) {
            this.selectedSettingsItem = selectedSettingsItem
            _settingsItems.value = mapSettingsItemsToSelectableObject()
        }
    }

    private fun createSettingsItemsAsync() {
        DefaultAppLogger.getInstance().i(Message.message {
            clazz = this@SettingsViewModel::class.java
            message = "createSettingsItemsAsync"
        })
        settingsItemsCreatingJob?.cancel()
        settingsItemsCreatingJob = viewModelScope.launch {
            settingsItemsList = createListOfSettingsItems()
            DefaultAppLogger.getInstance().i(Message.message {
                clazz = this@SettingsViewModel::class.java
                message = "CREATED"
            })
            _settingsItems.value = mapSettingsItemsToSelectableObject()
        }
    }

    private suspend fun createListOfSettingsItems(): List<SettingsItem> =
        withContext(Dispatchers.IO) {
            val settingsItems = mutableListOf<SettingsItem>()

            val currentNewsLocale = preferenceManager
                .newsLocale
                .firstOrNull()
                ?: DEFAULT_NEWS_LOCALE

            val currentIsSameLocale = preferenceManager
                .isSameLocale
                .firstOrNull()
                ?: false

            val currentUseMobileNetworkForImages = preferenceManager
                .useMobileNetworkForImages
                .firstOrNull()
                ?: true

            val currentAppLocale = getCurrentAppLocale()

            settingsItems.run {
                add(SettingsItem.SettingsItemPopup(
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
                            isEnabled = currentAppLocale.first.areLocalesEqual(key),
                            onClickListener = {
                                val wasLocaleChanged = changeAppLocale(key)
                                if (currentIsSameLocale) {
                                    val currentApplicationLocale = appLocalesToNewsLocales
                                        .getOrDefault(
                                            getCurrentAppLocale().first,
                                            DEFAULT_NEWS_LOCALE
                                        )

                                    changeNewsLocale(currentApplicationLocale)
                                }

                                if (wasLocaleChanged) {
                                    createSettingsItemsAsync()
                                }
                            }
                        )
                    }
                ))
            }

            settingsItems.run {
                add(SettingsItem.SettingsItemPopup(
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
                            isEnabled = currentNewsLocale.areLocalesEqual(key),
                            onClickListener = {
                                changeNewsLocale(key)
                            }
                        )
                    }
                ))
            }

            settingsItems.run {
                add(SettingsItem.SettingsItemSwitch(
                    id = SETTINGS_ITEM_SAME_LANGUAGE_ID,
                    icon = R.drawable.ic_language_24,
                    title = R.string.settings_item_title_use_same_language,
                    subTitle = null,
                    isChecked = currentIsSameLocale,
                    isEnabled = true,
                    onCheckedListener = { isChecked ->
                        changeUseSameLanguageForAppAndNews(isChecked)
                        if (isChecked) {
                            val currentApplicationLocale = appLocalesToNewsLocales
                                .getOrDefault(getCurrentAppLocale().first, DEFAULT_NEWS_LOCALE)

                            changeNewsLocale(currentApplicationLocale)
                        }
                    }
                ))
            }

            settingsItems.run {
                add(SettingsItem.SettingsItemSwitch(
                    id = SETTINGS_ITEM_USE_MOBILE_NETWORK_ID,
                    icon = R.drawable.ic_mobile_network_24,
                    title = R.string.settings_item_title_use_mobile_network_to_load_images,
                    subTitle = UiText.ResourceString(R.string.settings_item_subtitle_use_mobile_network_to_load_images),
                    isChecked = currentUseMobileNetworkForImages,
                    isEnabled = true,
                    onCheckedListener = { isChecked -> changeUseMobileNetworkForImages(isChecked) }
                ))
            }

            settingsItems.run {
                add(SettingsItem.SettingsItemScreen(
                    id = SETTINGS_ITEM_ABOUT_SCREEN_ID,
                    icon = R.drawable.ic_companion_white_24dp,
                    title = R.string.settings_item_title_about,
                    destinationId = R.id.FragmentSettingsAbout,
                    destinationClass = FragmentSettingsAbout::class,
                    args = null
                ))
            }

            settingsItems.toList()
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

    private fun mapSettingsItemsToSelectableObject(): List<SelectableObject<SettingsItem>> {
        val selectedSettingsItem = this.selectedSettingsItem

        val selectableSettingsItems =  settingsItemsList.map { settingsItem ->
            var isSelected = false
            if (selectedSettingsItem != null && settingsItem.id == selectedSettingsItem.id) {
                isSelected = settingsItem.isEnabled
            }

            SelectableObject(settingsItem, isSelected)
        }

        val selectedItem = selectableSettingsItems.find { settingsItem -> settingsItem.isSelected }
        if (selectedItem == null) {
            selectableSettingsItems
                .firstOrNull { it.data.isSelectable }
                ?.run {
                    _clearContainerEvent.value = Unit
                    val settingsItem = this.data
                    _showContentForSettingsItem.value = settingsItem
                    if (this@SettingsViewModel.selectedSettingsItem?.id != settingsItem.id) {
                        this@SettingsViewModel.selectedSettingsItem = settingsItem
                    }
                    isSelected = true
                }
        } else {
            _clearContainerEvent.value = Unit
            val settingsItem = selectedItem.data
            _showContentForSettingsItem.value = settingsItem
        }

        return selectableSettingsItems
    }

    private fun changeUseMobileNetworkForImages(use: Boolean) {
        viewModelScope.launch { preferenceManager.changeUseMobileNetworkForImages(use) }
    }

    private fun changeUseSameLanguageForAppAndNews(use: Boolean) {
        viewModelScope.launch {
            preferenceManager.changeUseSaveLanguageForAppAndNews(use)
        }
    }

    private fun changeNewsLocale(newNewsLocale: String): Boolean {
        assert(Looper.myLooper() == Looper.getMainLooper())
        var result = true

        viewModelScope.launch {
            val currentNewsLocale = preferenceManager
                .newsLocale
                .firstOrNull()
                ?: DEFAULT_NEWS_LOCALE

            if (currentNewsLocale == newNewsLocale) {
                result = false
                return@launch
            }

            preferenceManager.changeNewsLocale(newNewsLocale)
        }

        return result
    }

    private fun changeAppLocale(newAppLocale: String): Boolean {
        if (getCurrentAppLocale().first == newAppLocale) {
            return false
        }

        assert(Looper.myLooper() == Looper.getMainLooper())
        val appLocales = LocaleListCompat.forLanguageTags(newAppLocale)
        AppCompatDelegate.setApplicationLocales(appLocales)
        return true
    }
}
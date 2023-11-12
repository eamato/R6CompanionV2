package eamato.funn.r6companion.ui.viewmodels.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eamato.funn.r6companion.core.utils.Result
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.domain.entities.settings.SettingsAboutInfo
import eamato.funn.r6companion.domain.usecases.AboutUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val aboutUseCase: AboutUseCase
) : ViewModel() {

    private val _aboutInfo = MutableLiveData<UiState<SettingsAboutInfo>>()
    val aboutInfo: LiveData<UiState<SettingsAboutInfo>> = _aboutInfo

    init {
        getAboutInfo()
    }

    private fun getAboutInfo() {
        viewModelScope.launch {
            _aboutInfo.value = UiState.Progress

            when (val result = aboutUseCase()) {
                is Result.Success -> {
                    _aboutInfo.value = UiState.Success(result.data)
                }
                is Result.Error -> {
                    _aboutInfo.value = UiState.Error(result.error)
                }
            }
        }
    }
}
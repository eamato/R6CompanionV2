package eamato.funn.r6companion.ui.viewmodels.companion.weapons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.domain.usecases.WeaponsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import eamato.funn.r6companion.core.utils.Result

@HiltViewModel
class CompanionWeaponsViewModel @Inject constructor(
    private val weaponsUseCase: WeaponsUseCase
) : ViewModel() {

    private val _weaponsPlaceHolder = MutableLiveData<UiState<String>>()
    val weaponsPlaceHolder: LiveData<UiState<String>> = _weaponsPlaceHolder

    init {
        getWeaponsPlaceHolder()
    }

    private fun getWeaponsPlaceHolder() {
        viewModelScope.launch {
            _weaponsPlaceHolder.value = UiState.Progress

            when (val result = weaponsUseCase()) {
                is Result.Success -> {
                    _weaponsPlaceHolder.value = UiState.Success(result.data)
                }
                is Result.Error -> {
                    _weaponsPlaceHolder.value = UiState.Error(result.error)
                }
            }
        }
    }
}
package eamato.funn.r6companion.ui.viewmodels.companion.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eamato.funn.r6companion.core.utils.Result
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.domain.mappers.companion.MapsUseCaseMapper
import eamato.funn.r6companion.domain.entities.companion.maps.Map
import eamato.funn.r6companion.domain.usecases.MapsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanionMapsViewModel @Inject constructor(
    private val mapsUseCase: MapsUseCase
) : ViewModel() {

    private val _maps = MutableLiveData<UiState<List<Map>>>()
    val maps: LiveData<UiState<List<Map>>> = _maps

    init {
        getMaps()
    }

    private fun getMaps() {
        viewModelScope.launch {
            _maps.value = UiState.Progress

            when (val result = mapsUseCase(MapsUseCaseMapper)) {
                is Result.Success -> {
                    _maps.value = UiState.Success(result.data)
                }
                is Result.Error -> {
                    _maps.value = UiState.Error(result.error)
                }
            }
        }
    }
}
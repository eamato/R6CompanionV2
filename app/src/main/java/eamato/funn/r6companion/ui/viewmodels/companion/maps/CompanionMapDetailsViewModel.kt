package eamato.funn.r6companion.ui.viewmodels.companion.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eamato.funn.r6companion.core.utils.Result
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.domain.entities.companion.maps.MapDetails
import eamato.funn.r6companion.domain.mappers.companion.MapDetailsUseCaseMapper
import eamato.funn.r6companion.domain.usecases.MapDetailsUseCase
import eamato.funn.r6companion.ui.fragments.companion.FragmentMapDetailsArgs
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanionMapDetailsViewModel @Inject constructor(
    private val mapDetailsUseCase: MapDetailsUseCase,
    state: SavedStateHandle
) : ViewModel() {

    private val _mapDetails = MutableLiveData<UiState<MapDetails>>()
    val mapDetails: LiveData<UiState<MapDetails>> = _mapDetails

    init {
        getMapDetails(FragmentMapDetailsArgs.fromSavedStateHandle(state).mapId)
    }

    private fun getMapDetails(mapId: String) {
        viewModelScope.launch {
            _mapDetails.value = UiState.Progress

            when (val result = mapDetailsUseCase(MapDetailsUseCaseMapper, mapId)) {
                is Result.Success -> {
                    _mapDetails.value = UiState.Success(result.data)
                }
                is Result.Error -> {
                    _mapDetails.value = UiState.Error(result.error)
                }
            }
        }
    }
}
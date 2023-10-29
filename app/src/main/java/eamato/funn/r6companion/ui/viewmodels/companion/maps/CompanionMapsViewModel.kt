package eamato.funn.r6companion.ui.viewmodels.companion.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eamato.funn.r6companion.domain.mappers.companion.MapsUseCaseMapper
import eamato.funn.r6companion.domain.usecases.MapsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanionMapsViewModel @Inject constructor(
    private val mapsUseCase: MapsUseCase
) : ViewModel() {

    init {
        getMaps()
    }

    private fun getMaps() {
        viewModelScope.launch {
            mapsUseCase(MapsUseCaseMapper)
        }
    }
}
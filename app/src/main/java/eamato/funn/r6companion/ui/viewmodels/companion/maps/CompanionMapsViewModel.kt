package eamato.funn.r6companion.ui.viewmodels.companion.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import eamato.funn.r6companion.domain.entities.companion.maps.Map
import eamato.funn.r6companion.domain.usecases.MapsListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanionMapsViewModel @Inject constructor(
    private val mapsListUseCase: MapsListUseCase
) : ViewModel() {

    private val _maps = MutableLiveData<PagingData<Map>>()
    val maps: LiveData<PagingData<Map>> = _maps

    init {
        getMaps()
    }

    private fun getMaps() {
        viewModelScope.launch {
            mapsListUseCase().flow.cachedIn(viewModelScope).collect { pagingData ->
                _maps.value = pagingData
            }
        }
    }
}
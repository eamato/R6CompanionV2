package eamato.funn.r6companion.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainNavigationViewModel @Inject constructor() : ViewModel() {

    private val _backToGraphRootEvent = MutableSharedFlow<Unit>()
    val backToGraphRootEvent = _backToGraphRootEvent.asSharedFlow()

    fun submitBackToGraphRootEvent() {
        viewModelScope.launch {
            _backToGraphRootEvent.emit(Unit)
        }
    }
}
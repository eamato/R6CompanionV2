package eamato.funn.r6companion.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eamato.funn.r6companion.core.AppInitializer
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appInitializer: AppInitializer
) : ViewModel() {

    private val logger = DefaultAppLogger.getInstance()

    private val _isLoadingSplash = MutableStateFlow(true)
    val isLoadingSplash = _isLoadingSplash.asStateFlow()

    init {
        initializeApp()
    }

    private fun initializeApp() {
        viewModelScope.launch {
            appInitializer.initApp()
            _isLoadingSplash.value = false

            logger.i(Message.message {
                clazz = this@MainViewModel::class.java
                message = "App initialized"
            })
        }
    }
}
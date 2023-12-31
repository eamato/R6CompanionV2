package eamato.funn.r6companion.core.utils

sealed class UiState<out T> {
    data class Success<T>(val data: T, val additionalEvent: IAdditionalEvent? = null) : UiState<T>()
    data class Error(val error: Throwable) : UiState<Nothing>()
    object Progress : UiState<Nothing>()
}

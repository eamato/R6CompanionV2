package eamato.funn.r6companion.core.utils

sealed class Result<out T> {

    class Success<out T>(val data: T) : Result<T>()

    class Error(val error: Throwable) : Result<Nothing>()
}
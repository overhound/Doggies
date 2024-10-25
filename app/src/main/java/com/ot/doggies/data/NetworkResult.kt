package com.ot.doggies.data

sealed class NetworkResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
}

fun <T : Any> T.toSuccess(): NetworkResult<T> = NetworkResult.Success(this)
fun <T : Exception> T.toError(): NetworkResult<T> = NetworkResult.Error(this.localizedMessage ?: "Something bad happened.")
fun String.toError(): NetworkResult<String> = NetworkResult.Error(this)

suspend fun <T : Any, R : Any> NetworkResult<T>.mapSuccess(transform: suspend (T) -> R): NetworkResult<R> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.Success(transform(this.data))
        is NetworkResult.Error -> this
    }
}
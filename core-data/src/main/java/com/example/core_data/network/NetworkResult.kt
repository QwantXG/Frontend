package com.example.core_data.network

import com.example.core_data.model.ErrorResponse
import java.io.IOException
import kotlinx.serialization.SerializationException

sealed class NetworkResult<out T> {
    val isSuccessful: Boolean = false

    data class Success<T>(val data: T) : NetworkResult<T>()
    data class HttpError(val code: Int, val error: ErrorResponse? = null) : NetworkResult<Nothing>()
    data class NetworkError(val exception: IOException) : NetworkResult<Nothing>()
    data class SerializationError(val exception: SerializationException) : NetworkResult<Nothing>()
    data class UnknownError(val throwable: Throwable) : NetworkResult<Nothing>()

    inline fun <R> map(transform: (T) -> R): NetworkResult<R> = when (this) {
        is Success -> Success(transform(data))
        is HttpError -> this
        is NetworkError -> this
        is SerializationError -> this
        is UnknownError -> this
    }
}

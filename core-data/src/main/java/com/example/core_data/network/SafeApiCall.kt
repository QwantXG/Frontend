package com.example.core_data.network

import com.example.core_data.model.ErrorResponse
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.io.IOException

suspend inline fun <reified T> safeApiCall(
    json: Json = defaultJson(),
    crossinline block: suspend () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = block()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                NetworkResult.Success(body)
            } else if (T::class == Unit::class) {
                @Suppress("UNCHECKED_CAST")
                NetworkResult.Success(Unit as T)
            } else {
                NetworkResult.SerializationError(
                    SerializationException("Response body is null but ${T::class.simpleName} was expected")
                )
            }
        } else {
            val errorString = response.errorBody()?.string()
            val parsedError = errorString
                ?.takeIf { it.isNotBlank() }
                ?.let { runCatching { json.decodeFromString<ErrorResponse>(it) }.getOrNull() }

            NetworkResult.HttpError(response.code(), parsedError)
        }
    } catch (io: IOException) {
        NetworkResult.NetworkError(io)
    } catch (se: SerializationException) {
        NetworkResult.SerializationError(se)
    } catch (t: Throwable) {
        NetworkResult.UnknownError(t)
    }
}

package com.koniukhov.cinecircle.core.common.result

sealed class Result<out T> {

    data class Success<T>(val data: T) : Result<T>()

    data class Error(
        val exception: Throwable,
        val message: String = exception.message ?: "Unknown error occurred"
    ) : Result<Nothing>()

    object Loading : Result<Nothing>()

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    val isLoading: Boolean
        get() = this is Loading

    fun getDataOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun getErrorOrNull(): Throwable? = when (this) {
        is Error -> exception
        else -> null
    }
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

inline fun <T> Result<T>.onError(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Error) {
        action(exception)
    }
    return this
}

inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) {
        action()
    }
    return this
}

inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> Result.Error(exception, message)
        is Result.Loading -> Result.Loading
    }
}

suspend inline fun <T> safeCall(action: suspend () -> T): Result<T> {
    return try {
        Result.Success(action())
    } catch (e: Exception) {
        Result.Error(e)
    }
}
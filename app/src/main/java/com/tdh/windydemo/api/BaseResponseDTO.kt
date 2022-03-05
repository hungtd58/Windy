package com.tdh.windydemo.api

sealed class BaseResponseDTO<out T> {
    data class Success<out T>(val successData: T?) : BaseResponseDTO<T>()
    class Error(
        val exception: java.lang.Exception?,
        val message: String? = exception?.localizedMessage,
        val code: Int = -1
    ) : BaseResponseDTO<Nothing>()
}
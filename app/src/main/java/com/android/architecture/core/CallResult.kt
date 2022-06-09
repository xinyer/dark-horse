package com.android.architecture.core

data class CallResult<out T>(
    val code: Int?,
    val message: String? = "",
    val data: T? = null
) {
    companion object {
        const val UNKNOWN_ERROR = -1
        const val SERVER_ERROR = -2
        const val NETWORK_ERROR = -3
    }

    fun isSuccess(): Boolean = code == 0

    fun isFail(): Boolean = code == null || code > 0

    fun isUnknownError(): Boolean = code == UNKNOWN_ERROR

    fun isServerError(): Boolean = code == SERVER_ERROR

    fun isNetworkError(): Boolean = code == NETWORK_ERROR

}

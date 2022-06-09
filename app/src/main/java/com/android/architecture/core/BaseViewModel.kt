package com.android.architecture.core

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {
    protected val _successResult = MutableLiveData<SuccessResult?>()
    val successResult: LiveData<SuccessResult?> = _successResult

    protected val _errorResult = MutableLiveData<ErrorResult?>()
    val errorResult: LiveData<ErrorResult?> = _errorResult
}

open class SuccessResult(
    val message: String? = null,
    @StringRes val messageRes: Int = 0
) {
    fun getMessage(context: Context) = when {
        message != null -> message
        messageRes != 0 -> context.getString(messageRes)
        else -> null
    }
}

class ErrorResult(message: String? = null, messageRes: Int = 0): SuccessResult(message, messageRes)

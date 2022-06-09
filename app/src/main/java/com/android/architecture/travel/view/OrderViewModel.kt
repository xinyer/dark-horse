package com.android.architecture.travel.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.android.architecture.R
import com.android.architecture.core.BaseViewModel
import com.android.architecture.core.CallResult
import com.android.architecture.core.ErrorResult
import com.android.architecture.core.SuccessResult
import com.android.architecture.travel.model.OrderDomainModel
import com.android.architecture.travel.repository.OrderRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class OrderViewModelFactory(private val orderId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OrderViewModel(orderId) as T
    }
}

class OrderViewModel(
    private val orderId: String,
    private val orderRepository: OrderRepository = OrderRepository()
) : BaseViewModel() {

    private val _loadDataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val loadDataLoading: LiveData<Boolean> = _loadDataLoading

    private val _payRequestLoading: MutableLiveData<Boolean> = MutableLiveData()
    val payRequestLoading: LiveData<Boolean> = _payRequestLoading

    private val _retryLoading: MutableLiveData<Boolean> = MutableLiveData()
    val retryLoading: LiveData<Boolean> = _retryLoading

    private val _paymentErrorResult = MutableLiveData<ErrorResult?>()
    val paymentErrorResult: LiveData<ErrorResult?> = _paymentErrorResult

    private val _getOrderErrorResult = MutableLiveData<ErrorResult?>()
    val getOrderErrorResult: LiveData<ErrorResult?> = _getOrderErrorResult

    private val _order: MutableLiveData<OrderDomainModel?> = MutableLiveData()
    val order: LiveData<OrderDomainModel?> = _order

    val paymentStatus: LiveData<Boolean> = _order.map { it?.paid() == true }
    val paymentStatusText: LiveData<Int> = paymentStatus.map {
        if (it) R.string.enterprise_pay_success else R.string.enterprise_pay_none
    }
    val submitButtonText: LiveData<Int> = paymentStatus.map {
        if (it) R.string.enterprise_pay_success else R.string.enterprise_pay
    }

    init {
        getOrder()
    }

    fun getOrder() {
        viewModelScope.launch {
            _loadDataLoading.value = true
            _retryLoading.value = true
            val result = orderRepository.getOrder(orderId)
            handleGetOrderResult(result)
            _loadDataLoading.value = false
            _retryLoading.value = false
        }
    }

    fun enterprisePay() {
        viewModelScope.launch {
            _payRequestLoading.value = true
            val result = orderRepository.enterprisePay(orderId)
            handlePaymentResult(result)
            _payRequestLoading.value = false
        }
    }

    fun getEnterprisePayConfirmation() {
        viewModelScope.launch {
            _retryLoading.value = true
            val result = orderRepository.getEnterprisePayConfirmation(orderId)
            handlePaymentResult(result)
            _retryLoading.value = false
        }
    }

    private fun handlePaymentResult(result: CallResult<OrderDomainModel?>) {
        _order.value = result.data
        when {
            result.isSuccess() -> {
                if (!result.message.isNullOrEmpty()) {
                    _successResult.value = SuccessResult(message = result.message)
                }
            }
            result.isFail() -> {
                if (!result.message.isNullOrEmpty()) {
                    _errorResult.value = ErrorResult(message = result.message)
                }
            }
            result.isServerError() -> {
                _errorResult.value = ErrorResult(
                    messageRes = R.string.pay_error_generic_message
                )
            }
            result.isNetworkError() -> {
                _paymentErrorResult.value = ErrorResult(
                    messageRes = R.string.error_network_message
                )
            }
            result.isUnknownError() -> {
                _paymentErrorResult.value = ErrorResult(
                    messageRes = R.string.error_unknown_message
                )
            }
            else -> {
                Timber.d("not handle this result $result")
            }
        }
    }

    private fun handleGetOrderResult(result: CallResult<OrderDomainModel?>) {
        _order.value = result.data
        when {
            result.isSuccess() -> {
                if (!result.message.isNullOrEmpty()) {
                    _successResult.value = SuccessResult(message = result.message)
                }
            }
            result.isFail() -> {
                if (!result.message.isNullOrEmpty()) {
                    _errorResult.value = ErrorResult(message = result.message)
                }
            }
            result.isServerError() -> {
                _errorResult.value = ErrorResult(
                    messageRes = R.string.pay_error_generic_message
                )
            }
            result.isNetworkError() -> {
                _getOrderErrorResult.value = ErrorResult(
                    messageRes = R.string.error_network_message
                )
            }
            result.isUnknownError() -> {
                _getOrderErrorResult.value = ErrorResult(
                    messageRes = R.string.error_unknown_message
                )
            }
            else -> {
                Timber.d("not handle this result $result")
            }
        }
    }
}

package com.android.architecture.travel.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.architecture.R
import com.android.architecture.core.CallResult
import com.android.architecture.helper.CoroutineTestRule
import com.android.architecture.travel.TestDataHelper
import com.android.architecture.travel.TestDataHelper.failOrderCallResult
import com.android.architecture.travel.TestDataHelper.successOrderCallResult
import com.android.architecture.travel.TestDataHelper.successOrderDomainModel
import com.android.architecture.travel.model.OrderDomainModel
import com.android.architecture.travel.repository.OrderRepository
import java.net.SocketTimeoutException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class OrderViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: OrderViewModel
    private val repository: OrderRepository = mock()

    @Before
    fun setUp() = runTest {
        whenever(repository.getOrder(any())).thenReturn(
            CallResult(code = 0, message = "", data = successOrderDomainModel)
        )
        viewModel = OrderViewModel("20220605001", repository)
        viewModel.paymentStatus.observeForever { }
        viewModel.successResult.observeForever { }
        viewModel.errorResult.observeForever { }
        viewModel.paymentErrorResult.observeForever { }
        viewModel.getOrderErrorResult.observeForever { }
    }

    @After
    fun tearDown() {
        viewModel.paymentStatus.removeObserver { }
        viewModel.successResult.removeObserver { }
        viewModel.errorResult.removeObserver { }
        viewModel.paymentErrorResult.removeObserver { }
        viewModel.getOrderErrorResult.removeObserver { }
    }

    @Test
    fun enterprisePay_successMessageIsSuccessAndPaymentStatusIsTrue_repositoryReturnSuccess() =
        runTest {
            // given
            whenever(repository.enterprisePay(any(), any(), any())).thenReturn(
                successOrderCallResult
            )
            // when
            viewModel.enterprisePay()
            // then
            assertEquals("出票中", viewModel.successResult.value?.message)
            assertTrue(viewModel.paymentStatus.value!!)
        }

    @Test
    fun enterprisePay_errorMessageIsFailAndPaymentStatusIsFalse_repositoryReturnFail() =
        runTest {
            // given
            whenever(repository.enterprisePay(any(), any(), any())).thenReturn(failOrderCallResult)
            // when
            viewModel.enterprisePay()
            // then
            assertEquals("支付失败，机票价格超出公司差旅标准", viewModel.errorResult.value?.message)
            assertTrue(viewModel.paymentStatus.value!!)
        }

    @Test
    fun enterprisePay_errorMessageIsGenericErrorMessage_repositoryReturnServerError() =
        runTest {
            // given
            whenever(repository.enterprisePay(any(), any(), any())).thenReturn(
                CallResult(code = CallResult.SERVER_ERROR)
            )
            // when
            viewModel.enterprisePay()
            // then
            assertEquals(
                R.string.pay_error_generic_message,
                viewModel.errorResult.value?.messageRes
            )
        }

    @Test
    fun enterprisePay_errorMessageIsNetworkErrorMessage_repositoryReturnNetworkError() =
        runTest {
            // given
            whenever(repository.enterprisePay(any(), any(), any())).thenReturn(
                CallResult(code = CallResult.NETWORK_ERROR)
            )
            // when
            viewModel.enterprisePay()
            // then
            assertEquals(
                R.string.error_network_message,
                viewModel.paymentErrorResult.value?.messageRes
            )
        }

    @Test
    fun enterprisePay_errorMessageIsUnknownErrorMessage_repositoryReturnUnknownError() =
        runTest {
            // given
            whenever(repository.enterprisePay(any(), any(), any())).thenReturn(
                CallResult(code = CallResult.UNKNOWN_ERROR)
            )
            // when
            viewModel.enterprisePay()
            // then
            assertEquals(
                R.string.error_unknown_message,
                viewModel.paymentErrorResult.value?.messageRes
            )
        }

    @Test
    fun getOrder_orderIsSuccessOrder_repositoryReturnSuccessCallResult() =
        runTest {
            // given
            whenever(repository.getOrder(any())).thenReturn(successOrderCallResult)
            // when
            viewModel.getOrder()
            // then
            assertEquals(
                successOrderDomainModel,
                viewModel.order.value
            )
        }

    @Test
    fun getOrder_errorMessageIsGenericErrorMessage_repositoryReturnServerError() =
        runTest {
            // given
            whenever(repository.getOrder(any())).thenReturn(
                CallResult(code = CallResult.SERVER_ERROR)
            )
            // when
            viewModel.getOrder()
            // then
            assertEquals(
                R.string.pay_error_generic_message,
                viewModel.errorResult.value?.messageRes
            )
        }

    @Test
    fun getOrder_errorMessageIsNetworkErrorMessage_repositoryReturnNetworkError() =
        runTest {
            // given
            whenever(repository.getOrder(any())).thenReturn(
                CallResult(code = CallResult.NETWORK_ERROR)
            )
            // when
            viewModel.getOrder()
            // then
            assertEquals(
                R.string.error_network_message,
                viewModel.getOrderErrorResult.value?.messageRes
            )
        }

    @Test
    fun getOrder_errorMessageIsUnknownErrorMessage_repositoryReturnUnknownError() =
        runTest {
            // given
            whenever(repository.getOrder(any())).thenReturn(
                CallResult(code = CallResult.UNKNOWN_ERROR)
            )
            // when
            viewModel.getOrder()
            // then
            assertEquals(
                R.string.error_unknown_message,
                viewModel.getOrderErrorResult.value?.messageRes
            )
        }
}

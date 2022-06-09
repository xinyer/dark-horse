package com.android.architecture.travel.datasource

import com.android.architecture.core.RemoteClientBuilder
import com.android.architecture.extensions.enqueueFromFile
import com.android.architecture.helper.CoroutineTestRule
import com.android.architecture.travel.TestDataHelper
import com.android.architecture.travel.TestDataHelper.failPaymentDataModel
import com.android.architecture.travel.TestDataHelper.successOrderDataModel
import com.android.architecture.travel.TestDataHelper.successPaymentDataModel
import com.android.architecture.travel.model.OrderDataModel
import com.android.architecture.travel.model.PaymentDataModel
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OrderRemoteDataSourceTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private var mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start()
        RemoteClientBuilder.baseUrl = mockWebServer.url("/")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mockWebServer.shutdown()
    }

    @Test
    fun getOrder_returnSuccessOrderDataModel_serverReturnSuccessJson() = runTest {
        // given
        mockWebServer.enqueueFromFile(
            "response/order_get_success.json",
            HttpURLConnection.HTTP_OK
        )
        // when
        val result = OrderRemoteDataSource().getOrder("20220605001")
        // then
        assertTrue(result?.isSuccess() == true)
        assertEquals(successOrderDataModel, result)
    }

    @Test
    fun getOrder_returnFailOrderDataModel_serverReturnFailJson() = runTest {
        // given
        mockWebServer.enqueueFromFile(
            "response/order_get_fail.json",
            HttpURLConnection.HTTP_OK
        )
        // when
        val result = OrderRemoteDataSource().getOrder("20220605001")
        // then
        val expect = OrderDataModel(
            code = 1,
            message = "订单不存在",
            createAt = 1654492650000,
            order = null
        )
        assertTrue(result?.isSuccess() == false)
        assertEquals(expect, result)
    }

    @Test
    fun getOrder_throwTimeoutException_serverNoResponse() = runTest {
        assertFailsWith<SocketTimeoutException>(
            block = {
                mockWebServer.enqueue(
                    MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE)
                )
                OrderRemoteDataSource().getOrder("20220605001")
            }
        )
    }

    @Test
    fun enterprisePay_returnSuccessPaymentDataModel_serverReturnSuccessJson() = runTest {
        // given
        mockWebServer.enqueueFromFile(
            "response/order_enterprise_pay_success.json",
            HttpURLConnection.HTTP_OK
        )
        // when
        val result = OrderRemoteDataSource().enterprisePay("20220605001")
        // then
        assertTrue(result?.isSuccess() == true)
        assertEquals(successPaymentDataModel, result)
    }

    @Test
    fun enterprisePay_returnFailPaymentDataModel_serverReturnFailJson() = runTest {
        // given
        mockWebServer.enqueueFromFile(
            "response/order_enterprise_pay_fail.json",
            HttpURLConnection.HTTP_OK
        )
        // when
        val result = OrderRemoteDataSource().enterprisePay("20220605001")
        // then
        assertTrue(result?.isSuccess() == false)
        assertEquals(failPaymentDataModel, result)
    }

    @Test
    fun enterprisePay_throwTimeoutException_serverNoResponse() = runTest {
        assertFailsWith<SocketTimeoutException>(
            block = {
                mockWebServer.enqueue(
                    MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE)
                )
                OrderRemoteDataSource().enterprisePay("20220605001")
            }
        )
    }

    @Test
    fun getEnterprisePayConfirmation_returnSuccessPaymentDataModel_serverReturnSuccessJson() =
        runTest {
            // given
            mockWebServer.enqueueFromFile(
                "response/get_enterprise_pay_confirmation_success.json",
                HttpURLConnection.HTTP_OK
            )
            // when
            val result = OrderRemoteDataSource().getEnterprisePayConfirmation("20220605001")
            // then
            assertTrue(result?.isSuccess() == true)
            assertEquals(successPaymentDataModel, result)
        }

    @Test
    fun getEnterprisePayConfirmation_returnFailPaymentDataModel_serverReturnFailJson() = runTest {
        // given
        mockWebServer.enqueueFromFile(
            "response/get_enterprise_pay_confirmation_fail.json",
            HttpURLConnection.HTTP_OK
        )
        // when
        val result = OrderRemoteDataSource().getEnterprisePayConfirmation("20220605001")
        // then
        assertTrue(result?.isSuccess() == false)
        assertEquals(failPaymentDataModel, result)
    }

    @Test
    fun getEnterprisePayConfirmation_throwTimeoutException_serverNoResponse() = runTest {
        assertFailsWith<SocketTimeoutException>(
            block = {
                mockWebServer.enqueue(
                    MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE)
                )
                OrderRemoteDataSource().getEnterprisePayConfirmation("20220605001")
            }
        )
    }
}

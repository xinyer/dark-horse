package com.android.architecture.travel.repository

import com.android.architecture.travel.TestDataHelper.failOrderDomainModel
import com.android.architecture.travel.TestDataHelper.failPaymentDataModel
import com.android.architecture.travel.TestDataHelper.orderEntity
import com.android.architecture.travel.TestDataHelper.successOrderDataModel
import com.android.architecture.travel.TestDataHelper.successOrderDomainModel
import com.android.architecture.travel.TestDataHelper.successPaymentDataModel
import com.android.architecture.travel.datasource.OrderLocalDataSource
import com.android.architecture.travel.datasource.OrderRemoteDataSource
import com.android.architecture.travel.model.OrderDataModel
import com.android.architecture.travel.model.OrderEntity
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class OrderRepositoryTest {

    private val localDataSource: OrderLocalDataSource = mock()
    private val remoteDataSource: OrderRemoteDataSource = mock()

    @Test
    fun getOrder_returnOrderDomainModelAndSaveDatabase_remoteReturnSuccessModel() =
        runTest {
            // given
            whenever(remoteDataSource.getOrder(any())).thenReturn(successOrderDataModel)
            whenever(localDataSource.saveOrder(any())).thenReturn(Unit)
            val repository = OrderRepository(remoteDataSource, localDataSource)
            // when
            val result = repository.getOrder("20220605001")
            // then
            assertEquals(successOrderDomainModel, result.data)
            assertTrue(result.isSuccess())
            assertEquals("", result.message)
            verify(localDataSource).saveOrder(OrderEntity.mapFrom(successOrderDataModel)!!)
        }

    @Test
    fun getOrder_returnOrderDomainModelAndNotSaveDatabase_remoteReturnFailModel() =
        runTest {
            // given
            whenever(remoteDataSource.getOrder(any())).thenReturn(
                OrderDataModel(
                    code = 1,
                    message = "订单不存在",
                    order = null,
                    createAt = 1654492650000
                )
            )
            whenever(localDataSource.saveOrder(any())).thenReturn(Unit)
            val repository = OrderRepository(remoteDataSource, localDataSource)
            // when
            val result = repository.getOrder("20220605001")
            // then
            assertTrue(result.isFail())
            assertEquals("订单不存在", result.message)
            assertNull(result.data)
            verify(localDataSource, never()).saveOrder(OrderEntity.mapFrom(successOrderDataModel)!!)
        }

    @Test
    fun getOrder_returnOrderDomainModel_remoteThrowExceptionButLocalReturnEntity() =
        runTest {
            // given
            whenever(remoteDataSource.getOrder(any())).thenThrow(Exception())
            whenever(localDataSource.getOrder(any())).thenReturn(orderEntity)
            val repository = OrderRepository(remoteDataSource, localDataSource)
            // when
            val result = repository.getOrder("20220605001")
            // then
            assertEquals(successOrderDomainModel, result.data)
            assertTrue(result.isSuccess())
            assertEquals("", result.message)
            verify(localDataSource).getOrder("20220605001")
        }

    @Test
    fun getOrder_returnUnknownError_remoteThrowExceptionAndLocalReturnNull() =
        runTest {
            // given
            whenever(remoteDataSource.getOrder(any())).thenThrow(Exception())
            whenever(localDataSource.getOrder(any())).thenReturn(null)
            val repository = OrderRepository(remoteDataSource, localDataSource)
            // when
            val result = repository.getOrder("20220605001")
            // then
            assertTrue(result.isUnknownError())
            assertNull(result.data)
            assertEquals("", result.message)
        }

    @Test
    fun enterprisePay_returnOrderDomainModelAndSaveDatabase_remoteReturnSuccessModel() =
        runTest {
            // given
            whenever(remoteDataSource.enterprisePay(any())).thenReturn(successPaymentDataModel)
            whenever(localDataSource.saveOrder(any())).thenReturn(Unit)
            val repository = OrderRepository(remoteDataSource, localDataSource)
            // when
            val result = repository.enterprisePay("20220605001")
            // then
            assertEquals(successOrderDomainModel, result.data)
            assertTrue(result.isSuccess())
            assertEquals("出票中", result.message)
            verify(localDataSource).saveOrder(OrderEntity.mapFrom(successPaymentDataModel))
        }

    @Test
    fun enterprisePay_returnOrderDomainModelAndNotSaveDatabase_remoteReturnFailModel() =
        runTest {
            // given
            whenever(remoteDataSource.enterprisePay(any())).thenReturn(failPaymentDataModel)
            whenever(localDataSource.saveOrder(any())).thenReturn(Unit)
            val repository = OrderRepository(remoteDataSource, localDataSource)
            // when
            val result = repository.enterprisePay("20220605001")
            // then
            assertEquals(failOrderDomainModel, result.data)
            assertTrue(result.isFail())
            assertEquals("支付失败，机票价格超出公司差旅标准", result.message)
            verify(localDataSource, never()).saveOrder(OrderEntity.mapFrom(failPaymentDataModel))
        }

    @Test
    fun enterprisePay_returnServerErrorAndNotSaveDatabase_remoteThrowTimeoutException() =
        runTest {
            // given
            whenever(remoteDataSource.enterprisePay(any())).thenThrow(SocketTimeoutException())
            whenever(localDataSource.saveOrder(any())).thenReturn(Unit)
            val repository = OrderRepository(remoteDataSource, localDataSource)
            // when
            val result = repository.enterprisePay("20220605001")
            // then
            assertTrue(result.isServerError())
            assertNull(result.data)
            verify(localDataSource, never()).saveOrder(any())
        }

    @Test
    fun enterprisePay_returnNetworkErrorAndNotSaveDatabase_requestThrowIOExceptionAndConfirmThrowIOException() =
        runTest {
            // given
            whenever(remoteDataSource.enterprisePay(any())).thenThrow(IOException())
            whenever(remoteDataSource.getEnterprisePayConfirmation(any())).thenThrow(IOException())
            whenever(localDataSource.saveOrder(any())).thenReturn(Unit)
            val repository = OrderRepository(remoteDataSource, localDataSource)
            // when
            val result = repository.enterprisePay("20220605001")
            // then
            assertTrue(result.isNetworkError())
            assertNull(result.data)
            verify(localDataSource, never()).saveOrder(any())
        }

    @Test
    fun enterprisePay_returnOrderDomainModelAndSaveDatabase_requestThrowIOExceptionButConfirmReturnSuccess() =
        runTest {
            // given
            whenever(remoteDataSource.enterprisePay(any())).thenThrow(IOException())
            whenever(remoteDataSource.getEnterprisePayConfirmation(any())).thenReturn(
                successPaymentDataModel
            )
            whenever(localDataSource.saveOrder(any())).thenReturn(Unit)
            val repository = OrderRepository(remoteDataSource, localDataSource)
            // when
            val result = repository.enterprisePay("20220605001")
            // then
            assertEquals(successOrderDomainModel, result.data)
            assertTrue(result.isSuccess())
            assertEquals("出票中", result.message)
            verify(localDataSource).saveOrder(OrderEntity.mapFrom(successPaymentDataModel))
        }

    @Test
    fun enterprisePay_retry3TimesAndReturnServerErrorAndNotSaveDatabase_remoteThrowTimeoutException() =
        runTest {
            // given
            whenever(remoteDataSource.enterprisePay(any())).thenThrow(SocketTimeoutException())
            whenever(localDataSource.saveOrder(any())).thenReturn(Unit)
            val repository = OrderRepository(remoteDataSource, localDataSource)
            // when
            val result = repository.enterprisePay("20220605001")
            // then
            assertTrue(result.isServerError())
            assertNull(result.data)
            verify(remoteDataSource, times(3)).enterprisePay("20220605001")
            verify(localDataSource, never()).saveOrder(OrderEntity.mapFrom(successPaymentDataModel))
        }

    @Test
    fun getEnterprisePayConfirmation_returnOrderDomainModelAndSaveDatabase_remoteReturnSuccessModel() =
        runTest {
            // given
            whenever(remoteDataSource.getEnterprisePayConfirmation(any())).thenReturn(successPaymentDataModel)
            whenever(localDataSource.saveOrder(any())).thenReturn(Unit)
            val repository = OrderRepository(remoteDataSource, localDataSource)
            // when
            val result = repository.getEnterprisePayConfirmation("20220605001")
            // then
            assertEquals(successOrderDomainModel, result.data)
            assertTrue(result.isSuccess())
            assertEquals("出票中", result.message)
            verify(localDataSource).saveOrder(OrderEntity.mapFrom(successPaymentDataModel))
        }

    @Test
    fun getEnterprisePayConfirmation_returnOrderDomainModelAndNotSaveDatabase_remoteReturnFailModel() =
        runTest {
            // given
            whenever(remoteDataSource.getEnterprisePayConfirmation(any())).thenReturn(failPaymentDataModel)
            whenever(localDataSource.saveOrder(any())).thenReturn(Unit)
            val repository = OrderRepository(remoteDataSource, localDataSource)
            // when
            val result = repository.getEnterprisePayConfirmation("20220605001")
            // then
            assertEquals(failOrderDomainModel, result.data)
            assertTrue(result.isFail())
            assertEquals("支付失败，机票价格超出公司差旅标准", result.message)
            verify(localDataSource, never()).saveOrder(OrderEntity.mapFrom(failPaymentDataModel))
        }

    @Test
    fun getEnterprisePayConfirmation_returnNetworkErrorAndNotSaveDatabase_remoteThrowIOException() =
        runTest {
            // given
            whenever(remoteDataSource.getEnterprisePayConfirmation(any())).thenThrow(IOException())
            whenever(localDataSource.saveOrder(any())).thenReturn(Unit)
            val repository = OrderRepository(remoteDataSource, localDataSource)
            // when
            val result = repository.getEnterprisePayConfirmation("20220605001")
            // then
            assertTrue(result.isNetworkError())
            assertNull(result.data)
            verify(localDataSource, never()).saveOrder(any())
        }
}

package com.android.architecture.travel.repository

import com.android.architecture.db.AppDatabase
import com.android.architecture.MainApplication
import com.android.architecture.core.CallResult
import com.android.architecture.travel.datasource.OrderLocalDataSource
import com.android.architecture.travel.datasource.OrderRemoteDataSource
import com.android.architecture.travel.model.OrderDataModel
import com.android.architecture.travel.model.OrderDomainModel
import com.android.architecture.travel.model.OrderEntity
import com.android.architecture.travel.model.PaymentDataModel
import java.io.IOException
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlinx.coroutines.delay

class OrderRepository(
    private val remoteDataSource: OrderRemoteDataSource = OrderRemoteDataSource(),
    private val localDataSource: OrderLocalDataSource = OrderLocalDataSource(
        AppDatabase.instance(
            MainApplication.context
        )
    )
) {

    suspend fun getOrder(id: String): CallResult<OrderDomainModel> {
        try {
            val response = remoteDataSource.getOrder(id)
            saveToLocal(response)
            return CallResult(
                code = response?.code,
                message = response?.message,
                data = OrderDomainModel.mapFrom(response)
            )
        } catch (e: Exception) {
            val order = localDataSource.getOrder(id)
            return if (order != null) {
                CallResult(code = 0, data = OrderDomainModel.mapFrom(order))
            } else {
                CallResult(CallResult.UNKNOWN_ERROR)
            }
        }
    }

    suspend fun enterprisePay(
        orderId: String,
        delay: Long = 5000,
        retryCount: Int = 3
    ): CallResult<OrderDomainModel?> {
        try {
            if (retryCount == 0) {
                return CallResult(CallResult.SERVER_ERROR)
            }
            val response = remoteDataSource.enterprisePay(orderId)
            saveToLocal(response)
            return CallResult(
                code = response?.code,
                message = response?.message,
                data = OrderDomainModel.mapFrom(response)
            )
        } catch (e: SocketTimeoutException) {
            delay(delay)
            return enterprisePay(orderId, delay, retryCount - 1)
        } catch (e: IOException) {
            return getEnterprisePayConfirmation(orderId)
        }
    }

    suspend fun getEnterprisePayConfirmation(orderId: String): CallResult<OrderDomainModel?> {
        return try {
            val response = remoteDataSource.getEnterprisePayConfirmation(orderId)
            saveToLocal(response)
            CallResult(
                code = response?.code,
                message = response?.message,
                data = OrderDomainModel.mapFrom(response)
            )
        } catch (e: IOException) {
            CallResult(CallResult.NETWORK_ERROR)
        }
    }

    private suspend fun saveToLocal(data: PaymentDataModel?) {
        if (data?.isSuccess() == true) {
            localDataSource.saveOrder(OrderEntity.mapFrom(data))
        }
    }

    private suspend fun saveToLocal(data: OrderDataModel?) {
        if (data?.isSuccess() == true) {
            OrderEntity.mapFrom(data)?.let {
                localDataSource.saveOrder(it)
            }
        }
    }
}

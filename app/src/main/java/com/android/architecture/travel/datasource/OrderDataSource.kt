package com.android.architecture.travel.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.architecture.db.AppDatabase
import com.android.architecture.core.RemoteClientBuilder
import com.android.architecture.travel.model.OrderDataModel
import com.android.architecture.travel.model.OrderEntity
import com.android.architecture.travel.model.PaymentDataModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


class OrderRemoteDataSource {

    interface OrderApi {
        @GET("/order-proposals/{pid}/contract")
        suspend fun getOrder(@Path("pid") proposalId: String): OrderDataModel?

        @POST("/order-contracts/{oid}/enterprise-payment")
        suspend fun enterprisePay(@Path("oid") orderId: String): PaymentDataModel?

        @GET("/order-contracts/{oid}/enterprise-payment/confirmation")
        suspend fun getEnterprisePayConfirmation(@Path("oid") orderId: String): PaymentDataModel?
    }

    @Throws(Exception::class)
    suspend fun getOrder(id: String): OrderDataModel? {
        val service = RemoteClientBuilder.buildRetrofit().create(OrderApi::class.java)
        return service.getOrder(id)
    }

    @Throws(Exception::class)
    suspend fun enterprisePay(orderId: String): PaymentDataModel? {
        val service = RemoteClientBuilder.buildRetrofit().create(OrderApi::class.java)
        return service.enterprisePay(orderId)
    }

    @Throws(Exception::class)
    suspend fun getEnterprisePayConfirmation(orderId: String): PaymentDataModel? {
        val service = RemoteClientBuilder.buildRetrofit().create(OrderApi::class.java)
        return service.getEnterprisePayConfirmation(orderId)
    }
}

class OrderLocalDataSource(private val database: AppDatabase) {

    @Dao
    interface OrderDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun saveOrder(orderEntity: OrderEntity)

        @Query("SELECT * FROM T_ORDER WHERE id = :orderId")
        suspend fun getOrder(orderId: String): OrderEntity?
    }

    suspend fun saveOrder(orderEntity: OrderEntity) {
        database.orderDao().saveOrder(orderEntity)
    }

    suspend fun getOrder(orderId: String): OrderEntity? {
        return database.orderDao().getOrder(orderId)
    }
}

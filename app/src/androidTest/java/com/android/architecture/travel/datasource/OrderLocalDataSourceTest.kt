package com.android.architecture.travel.datasource

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.architecture.db.AppDatabase
import com.android.architecture.travel.model.OrderEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class OrderLocalDataSourceTest {
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun getOrder_returnSameEntity_saveOrder() = runBlocking {
        // given
        val orderEntity = OrderEntity(
            id = "20220605001",
            origin = "西安",
            destination = "北京",
            originAirport = "咸阳机场",
            destinationAirport = "大兴国际机场",
            departureTime = "9:30",
            landingTime = "12:45",
            airlines = "东方航空",
            departureDate = "20220605",
            price = 580.0,
            passengerId = "20200604110",
            passengerName = "王鑫",
            passengerDepartment = "Telecom Digital",
            type = 0,
            paymentStatus = 0,
            createAt = 1654413450000,
            expireAt = 1654499850000
        )
        // when
        val orderLocalDataSource = OrderLocalDataSource(db)
        orderLocalDataSource.saveOrder(orderEntity)
        // then
        val actual = orderLocalDataSource.getOrder("20220605001")
        assertEquals(orderEntity, actual)
    }
}

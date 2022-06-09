package com.android.architecture.travel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_order")
data class OrderEntity(
    @PrimaryKey
    val id: String,
    val airlines: String,
    val createAt: Long,
    val departureDate: String,
    val departureTime: String,
    val destination: String,
    val destinationAirport: String,
    val expireAt: Long,
    val landingTime: String,
    val origin: String,
    val originAirport: String,
    val passengerId: String,
    val passengerName: String,
    val passengerDepartment: String,
    val paymentStatus: Int,
    val price: Double,
    val type: Int
) {
    companion object {
        fun mapFrom(data: PaymentDataModel) : OrderEntity {
            val order = data.order
            return OrderEntity(
                id = order.id,
                airlines = order.airlines,
                createAt = order.createAt,
                departureDate = order.departureDate,
                departureTime = order.departureTime,
                destination = order.destination,
                destinationAirport = order.destinationAirport,
                expireAt = order.expireAt,
                landingTime = order.landingTime,
                origin = order.origin,
                originAirport = order.originAirport,
                passengerId = order.passenger.id,
                passengerName = order.passenger.name,
                passengerDepartment = order.passenger.department,
                paymentStatus = order.paymentStatus,
                price = order.price,
                type = order.type
            )
        }

        fun mapFrom(data: OrderDataModel) : OrderEntity? {
            val order = data.order
            return order?.let {
                OrderEntity(
                    id = it.id,
                    airlines = it.airlines,
                    createAt = it.createAt,
                    departureDate = it.departureDate,
                    departureTime = it.departureTime,
                    destination = it.destination,
                    destinationAirport = it.destinationAirport,
                    expireAt = it.expireAt,
                    landingTime = it.landingTime,
                    origin = it.origin,
                    originAirport = it.originAirport,
                    passengerId = it.passenger.id,
                    passengerName = it.passenger.name,
                    passengerDepartment = it.passenger.department,
                    paymentStatus = it.paymentStatus,
                    price = it.price,
                    type = it.type
                )
            }
        }
    }
}

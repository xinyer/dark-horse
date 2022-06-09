package com.android.architecture.travel.model

import com.squareup.moshi.Json

data class PaymentDataModel(
    val code: Int,
    @Json(name = "create_at")
    val createAt: Long,
    val message: String,
    val order: Order
) {
    fun isSuccess() = code == 0

    data class Order(
        val airlines: String,
        @Json(name = "create_at")
        val createAt: Long,
        @Json(name = "departure_date")
        val departureDate: String,
        @Json(name = "departure_time")
        val departureTime: String,
        val destination: String,
        @Json(name = "destination_airport")
        val destinationAirport: String,
        @Json(name = "expire_at")
        val expireAt: Long,
        val id: String,
        @Json(name = "landing_time")
        val landingTime: String,
        val origin: String,
        @Json(name = "origin_airport")
        val originAirport: String,
        val passenger: Passenger,
        @Json(name = "payment_status")
        val paymentStatus: Int,
        val price: Double,
        val type: Int
    )

    data class Passenger(
        val department: String,
        val id: String,
        val name: String
    )
}

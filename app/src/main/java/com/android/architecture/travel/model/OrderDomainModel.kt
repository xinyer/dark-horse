package com.android.architecture.travel.model

data class OrderDomainModel(
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
    val passenger: Passenger,
    val paymentStatus: PaymentStatus,
    val price: Double,
    val type: Int
) {
    companion object {
        fun mapFrom(data: PaymentDataModel?): OrderDomainModel? {
            val order = data?.order
            return order?.let {
                OrderDomainModel(
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
                    passenger = Passenger(
                        id = it.passenger.id,
                        name = it.passenger.name,
                        department = it.passenger.department,
                    ),
                    paymentStatus = PaymentStatus.values()[it.paymentStatus],
                    price = it.price,
                    type = it.type
                )
            }
        }

        fun mapFrom(data: OrderDataModel?): OrderDomainModel? {
            val order = data?.order
            return order?.let {
                OrderDomainModel(
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
                    passenger = Passenger(
                        id = it.passenger.id,
                        name = it.passenger.name,
                        department = it.passenger.department,
                    ),
                    paymentStatus = PaymentStatus.values()[it.paymentStatus],
                    price = it.price,
                    type = order.type
                )
            }
        }

        fun mapFrom(entity: OrderEntity): OrderDomainModel {
            return OrderDomainModel(
                id = entity.id,
                airlines = entity.airlines,
                createAt = entity.createAt,
                departureDate = entity.departureDate,
                departureTime = entity.departureTime,
                destination = entity.destination,
                destinationAirport = entity.destinationAirport,
                expireAt = entity.expireAt,
                landingTime = entity.landingTime,
                origin = entity.origin,
                originAirport = entity.originAirport,
                passenger = Passenger(
                    id = entity.passengerId,
                    name = entity.passengerName,
                    department = entity.passengerDepartment,
                ),
                paymentStatus = PaymentStatus.values()[entity.paymentStatus],
                price = entity.price,
                type = entity.type
            )
        }
    }

    fun paid() = paymentStatus == PaymentStatus.PAID

    fun id() = "#$id"

    fun title() = "$origin - $destination"

    fun origin() = "$departureTime $origin $originAirport"

    fun destination() = "$landingTime $destination $destinationAirport"

    fun passenger() = "${passenger.name}\n${passenger.department}"

    fun price() = "¥$price"

    data class Passenger(
        val id: String,
        val name: String,
        val department: String,
    )

    enum class PaymentStatus {
        NONE, // 未支付
        PAID, // 已支付
    }
}

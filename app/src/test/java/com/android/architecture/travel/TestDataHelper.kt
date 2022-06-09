package com.android.architecture.travel

import com.android.architecture.core.CallResult
import com.android.architecture.travel.model.OrderDataModel
import com.android.architecture.travel.model.OrderDomainModel
import com.android.architecture.travel.model.OrderEntity
import com.android.architecture.travel.model.PaymentDataModel

object TestDataHelper {

    val successOrderDataModel = OrderDataModel(
        code = 0,
        message = "",
        createAt = 1654492650000,
        order = OrderDataModel.Order(
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
            passenger = OrderDataModel.Passenger(
                id = "20200604110",
                name = "王鑫",
                department = "Telecom Digital"
            ),
            type = 0,
            paymentStatus = 1,
            createAt = 1654413450000,
            expireAt = 1654499850000
        )
    )

    val successPaymentDataModel = PaymentDataModel(
        code = 0,
        message = "出票中",
        createAt = 1654492650000,
        order = PaymentDataModel.Order(
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
            passenger = PaymentDataModel.Passenger(
                id = "20200604110",
                name = "王鑫",
                department = "Telecom Digital"
            ),
            type = 0,
            paymentStatus = 1,
            createAt = 1654413450000,
            expireAt = 1654499850000
        )
    )

    val failPaymentDataModel = PaymentDataModel(
        code = 1,
        message = "支付失败，机票价格超出公司差旅标准",
        createAt = 1654492650000,
        order = PaymentDataModel.Order(
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
            passenger = PaymentDataModel.Passenger(
                id = "20200604110",
                name = "王鑫",
                department = "Telecom Digital"
            ),
            type = 0,
            paymentStatus = 0,
            createAt = 1654413450000,
            expireAt = 1654499850000
        )
    )

    val successOrderDomainModel = OrderDomainModel(
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
        passenger = OrderDomainModel.Passenger(
            id = "20200604110",
            name = "王鑫",
            department = "Telecom Digital"
        ),
        type = 0,
        paymentStatus = OrderDomainModel.PaymentStatus.PAID,
        createAt = 1654413450000,
        expireAt = 1654499850000
    )

    val failOrderDomainModel = OrderDomainModel(
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
        passenger = OrderDomainModel.Passenger(
            id = "20200604110",
            name = "王鑫",
            department = "Telecom Digital"
        ),
        type = 0,
        paymentStatus = OrderDomainModel.PaymentStatus.NONE,
        createAt = 1654413450000,
        expireAt = 1654499850000
    )

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
        paymentStatus = 1,
        createAt = 1654413450000,
        expireAt = 1654499850000
    )

    val successOrderCallResult = CallResult(
        code = 0,
        message = "出票中",
        data = successOrderDomainModel
    )

    val failOrderCallResult = CallResult(
        code = 1,
        message = "支付失败，机票价格超出公司差旅标准",
        data = OrderDomainModel(
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
            passenger = OrderDomainModel.Passenger(
                id = "20200604110",
                name = "王鑫",
                department = "Telecom Digital"
            ),
            type = 0,
            paymentStatus = OrderDomainModel.PaymentStatus.PAID,
            createAt = 1654413450000,
            expireAt = 1654499850000
        )
    )
}

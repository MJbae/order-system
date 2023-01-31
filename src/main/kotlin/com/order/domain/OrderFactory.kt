package com.order.domain

import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class OrderFactory {
    fun create(freeDeliveryThreshold: BigDecimal, deliveryCharge: BigDecimal): Order {
        return Order(
            id = null,
            price = BigDecimal.ZERO,
            orderItems = arrayListOf(),
            freeDeliveryThreshold = freeDeliveryThreshold,
            deliveryCharge = deliveryCharge
        )
    }
}

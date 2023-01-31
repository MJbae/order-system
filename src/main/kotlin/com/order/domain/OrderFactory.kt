package com.order.domain

import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class OrderFactory {
    fun create(freeDeliveryLimit: BigDecimal, deliveryFee: BigDecimal): Order {
        return Order(
            id = null,
            price = BigDecimal.ZERO,
            orderItems = arrayListOf(),
            freeDeliveryLimit = freeDeliveryLimit,
            deliveryFee = deliveryFee
        )
    }
}

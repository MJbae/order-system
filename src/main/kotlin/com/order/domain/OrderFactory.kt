package com.order.domain

import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class OrderFactory {
    fun create(freeDeliveryThreshold: BigDecimal, deliveryCharge: BigDecimal): Order {
        return Order(
            id = null,
            price = OrderPrice(
                value = BigDecimal.ZERO,
                freeDeliveryThreshold = freeDeliveryThreshold,
                deliveryCharge = deliveryCharge
            ),
            orderItems = arrayListOf()
        )
    }
}

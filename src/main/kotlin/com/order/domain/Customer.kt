package com.order.domain

import com.order.cli.dto.OrderData
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class Customer {

    fun order(orderData: OrderData): Order {
        val order = Order()
        val item = orderData.item
        var totalPrice = BigDecimal.ZERO

        totalPrice = this.calculatePriceWith(totalPrice, item!!.price, orderData.orderQuantity)

        item.decreaseStock(orderData.orderQuantity)

        order.addDeliveryFeeByAmountLimitTo(totalPrice)

        order.updateWith(order.orderItems)

        return order
    }

    private fun calculatePriceWith(totalPrice: BigDecimal, itemPrice: BigDecimal, orderQuantity: Int): BigDecimal {
        val priceSum = orderQuantity.times(itemPrice.toLong())
        return totalPrice + BigDecimal.valueOf(priceSum)
    }
}

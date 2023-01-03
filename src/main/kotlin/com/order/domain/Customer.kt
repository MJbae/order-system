package com.order.domain

import com.order.cli.dto.OrderData
import com.order.cli.dto.OrderResult
import com.order.exception.SoldOutException
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class Customer {

    fun order(orderData: OrderData): OrderResult {
        val order = Order()
        val item = orderData.item
        var totalPrice = BigDecimal.ZERO

        totalPrice = this.calculatePriceWith(totalPrice, item!!.price, orderData.orderQuantity)

        try {
            item.decreaseStock(orderData.orderQuantity)
        } catch (e: SoldOutException) {
            return OrderResult(false, null)
        }

        order.addDeliveryFeeByAmountLimitTo(totalPrice)

        order.updateWith(order.orderItems)

        return OrderResult(true, order)
    }

    private fun calculatePriceWith(totalPrice: BigDecimal, itemPrice: BigDecimal, orderQuantity: Int): BigDecimal {
        val priceSum = orderQuantity.times(itemPrice.toLong())
        return totalPrice + BigDecimal.valueOf(priceSum)
    }
}

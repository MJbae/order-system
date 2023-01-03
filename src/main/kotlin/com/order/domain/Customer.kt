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
            return OrderResult(false, totalPrice, arrayListOf())
        }

        order.addDeliveryFeeByAmountLimitTo(totalPrice)

        return OrderResult(true, order.price, arrayListOf())
    }

    private fun calculatePriceWith(totalPrice: BigDecimal, itemPrice: BigDecimal, orderQuantity: Int): BigDecimal {
        val priceSum = orderQuantity.times(itemPrice.toLong())
        return totalPrice + BigDecimal.valueOf(priceSum)
    }
}

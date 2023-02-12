package com.order.application

import com.order.domain.OrderItem
import com.order.domain.OrderPrice
import com.order.domain.OrderPrice.Companion.ZERO

class OrderResult(
    val isSuccess: Boolean,
    val price: OrderPrice,
    val orderItems: List<OrderItem>
) {
    fun add(new: OrderResult): OrderResult {
        val newOrderPrice = this.price.addValue(new.price)
        val newOrderItems = this.orderItems + new.orderItems
        return OrderResult(this.isSuccess, newOrderPrice, newOrderItems)
    }

    constructor() : this(
        isSuccess = true,
        price = ZERO,
        orderItems = arrayListOf()
    )
}

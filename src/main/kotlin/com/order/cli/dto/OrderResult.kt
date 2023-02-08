package com.order.cli.dto

import com.order.domain.OrderItem
import com.order.domain.OrderPrice

class OrderResult(
    val isSuccess: Boolean,
    val price: OrderPrice?,
    val orderItems: List<OrderItem>
)

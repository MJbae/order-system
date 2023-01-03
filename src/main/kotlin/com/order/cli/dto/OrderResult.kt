package com.order.cli.dto

import com.order.domain.OrderItem
import java.math.BigDecimal

class OrderResult(
    val isSuccess: Boolean,
    val price: BigDecimal?,
    val orderItems: MutableList<OrderItem>
)

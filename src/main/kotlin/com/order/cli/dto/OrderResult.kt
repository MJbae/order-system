package com.order.cli.dto

import com.order.domain.Order

class OrderResult(
    val isSuccess: Boolean,
    val order: Order?
)

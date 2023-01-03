package com.order.cli.dto

import com.order.domain.Item

class OrderData(
    val itemId: Long,
    val orderQuantity: Int,
    var item: Item?
)

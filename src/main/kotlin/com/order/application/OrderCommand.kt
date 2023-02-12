package com.order.application

import com.order.domain.Item

data class OrderCommand(
    val itemId: Long,
    val orderQuantity: Int,
    val item: Item?
) {
    constructor(itemId: Long, orderQuantity: Int) : this(
        itemId = itemId,
        orderQuantity = orderQuantity,
        item = null
    )

    fun addItem(item: Item): OrderCommand {
        return OrderCommand(this.itemId, this.orderQuantity, item)
    }
}

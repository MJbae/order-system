package com.order.cli.interfaces

interface OrderItemPrinter<T> {
    fun showBy(order: T)
}

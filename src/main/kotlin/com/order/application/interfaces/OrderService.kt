package com.order.application.interfaces

interface OrderService<T, S> {
    fun order(orderData: S): T
}

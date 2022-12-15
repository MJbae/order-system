package com.order.application.interfaces

interface OrderLogic<T, S> {
    fun order(orderData: List<S>): T
}

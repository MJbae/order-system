package com.order.cli.printer

import com.order.cli.interfaces.OrderItemPrinter
import com.order.domain.Order
import com.order.domain.OrderItem
import com.order.infra.OrderRepository
import org.springframework.stereotype.Component

@Component
class OrderProductPrinter(
    private val orderRepository: OrderRepository
) : OrderItemPrinter<Order> {
    override fun showBy(order: Order) {
        println("- - - - - - - - - - - - - - - - - - - -")
        order.orderItems.forEach { orderItem -> showOrderItemMessage(orderItem) }

        println("- - - - - - - - - - - - - - - - - - - -")
        this.showPriceOrdering(order.orderItems)

        println("- - - - - - - - - - - - - - - - - - - -")
        this.showPricePaying(order)
    }

    private fun showPricePaying(order: Order) {
        println("지불금액: ${order.price}원")
    }
    private fun showPriceOrdering(orderItems: List<OrderItem>) {
        println("주문금액: 준비중...")
    }
    private fun showOrderItemMessage(orderItem: OrderItem) {
        println("${orderItem.item.name} - ${orderItem.quantity}개")
    }
}

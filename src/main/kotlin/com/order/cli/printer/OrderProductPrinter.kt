package com.order.cli.printer

import com.order.cli.dto.OrderResult
import com.order.cli.interfaces.OrderItemPrinter
import com.order.domain.OrderItem
import com.order.infra.OrderRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class OrderProductPrinter(
    private val orderRepository: OrderRepository
) : OrderItemPrinter<OrderResult> {
    override fun showBy(order: OrderResult) {
        println("- - - - - - - - - - - - - - - - - - - -")
        order.orderItems.forEach { orderItem -> showOrderItemMessage(orderItem) }

        println("- - - - - - - - - - - - - - - - - - - -")
        this.showPriceOrdering(order.orderItems)

        println("- - - - - - - - - - - - - - - - - - - -")
        this.showPricePaying(order.price?.value)
    }

    private fun showPricePaying(price: BigDecimal?) {
        if (price == null) {
            println("지불금액이 계산되지 않았습니다.")
        }

        println("지불금액: ${price}원")
    }

    private fun showPriceOrdering(orderItems: List<OrderItem>) {
        orderItems.forEach { orderItem ->
            println("${orderItem.item.name}는 개당 ${orderItem.item.price}원, ${orderItem.quantity}개 주문")
        }
    }

    private fun showOrderItemMessage(orderItem: OrderItem) {
        println("${orderItem.item.name} - ${orderItem.quantity}개")
    }
}

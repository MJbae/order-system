package com.order.cli.printer

import com.order.cli.interfaces.OrderItemPrinter
import com.order.domain.Order
import org.springframework.stereotype.Component

@Component
class OrderProductPrinter : OrderItemPrinter<Order> {
    override fun showBy(order: Order) {
        println("- - - - - - - - - - - - - - - - - - - -")

        println("- - - - - - - - - - - - - - - - - - - -")

        println("- - - - - - - - - - - - - - - - - - - -")
        this.showPricePaying(order)
    }

    private fun showPricePaying(order: Order) {
        println("지불금액: ${order.price}원")
    }
}

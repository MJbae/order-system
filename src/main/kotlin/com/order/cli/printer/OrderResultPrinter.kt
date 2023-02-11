package com.order.cli.printer

import com.order.cli.dto.OrderResult
import com.order.cli.interfaces.ResultPrinter
import org.springframework.stereotype.Component

@Component
class OrderResultPrinter : ResultPrinter<OrderResult> {
    override fun showBy(result: OrderResult) {
        println("- - - - - - - - - - - - - - - - - - - -")
        this.showOrderItemMessage(result)

        println("- - - - - - - - - - - - - - - - - - - -")
        this.showPriceToOrder(result)

        println("- - - - - - - - - - - - - - - - - - - -")
        this.showPriceToPay(result)
    }

    private fun showPriceToPay(orderResult: OrderResult) {
        val orderPrice = orderResult.price
        if (orderPrice == null) {
            println("지불금액이 계산되지 않았습니다.")
            return
        }

        println("지불금액: ${orderPrice.value}원")
    }

    private fun showPriceToOrder(orderResult: OrderResult) {
        orderResult.orderItems.forEach { orderItem ->
            println("${orderItem.item.name}는 개당 ${orderItem.item.price}원, ${orderItem.quantity}개 주문")
        }
    }

    private fun showOrderItemMessage(orderResult: OrderResult) {
        orderResult.orderItems.forEach { orderItem ->
            println(
                "${orderItem.item.name} - ${orderItem.quantity}개"
            )
        }
    }
}

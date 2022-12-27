package com.order.application

import com.order.application.interfaces.OrderLogic
import com.order.cli.dto.OrderData
import com.order.domain.Item
import com.order.domain.Order
import com.order.infra.ItemRepository
import com.order.infra.OrderRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.transaction.Transactional

@Service
@Transactional
class OrderService(
    private val orderRepository: OrderRepository,
    private val itemRepository: ItemRepository,
) : OrderLogic<Order, OrderData> {

    override fun order(orderData: List<OrderData>): Order {
        val order = Order()
        var totalPrice = BigDecimal.ZERO

        for (each in orderData) {
            val item: Item = itemRepository.findByIdInLock(each.itemId)

            item.decreaseStock(each.itemQuantity)

            totalPrice = item.calculatePriceWith(totalPrice, each.itemQuantity)

            itemRepository.save(item)
        }

        order.addDeliveryFeeByAmountLimit(totalPrice)

        this.updateWith(order)

        return order
    }

    private fun updateWith(
        order: Order
    ) {
        order.updateWith(order.orderItems)
        orderRepository.save(order)
    }
}

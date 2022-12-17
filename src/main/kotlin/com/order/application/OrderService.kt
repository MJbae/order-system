package com.order.application

import com.order.application.interfaces.OrderLogic
import com.order.cli.dto.OrderData
import com.order.domain.Item
import com.order.domain.Order
import com.order.domain.OrderItem
import com.order.infra.ItemRepository
import com.order.infra.OrderItemRepository
import com.order.infra.OrderRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.transaction.Transactional

@Service
@Transactional
class OrderService(
    private val orderRepository: OrderRepository,
    private val itemRepository: ItemRepository,
    private val orderItemRepository: OrderItemRepository,
    private val freeDeliveryLimit: BigDecimal = BigDecimal(50000),
    private val deliveryFee: BigDecimal = BigDecimal(2500)
) : OrderLogic<Order, OrderData> {
    override fun order(orderData: List<OrderData>): Order {
        val order = Order()
        val orderItems: ArrayList<OrderItem> = ArrayList()
        var totalPrice = BigDecimal.ZERO

        for (each in orderData) {
            val item: Item = itemRepository.findByIdInLock(each.itemId)
            val orderItem = OrderItem(order, item, each.itemQuantity)

            orderItems.add(orderItem)

            val priceSum = each.itemQuantity.times(item.price.toLong())
            totalPrice += BigDecimal.valueOf(priceSum)

            itemRepository.save(item)
        }

        addDeliveryFeeByAmountLimit(freeDeliveryLimit, order, totalPrice, deliveryFee)

        orderRepository.save(order)

        for (each in orderItems) {
            orderItemRepository.save(each)
        }

        return order
    }

    private fun addDeliveryFeeByAmountLimit(
        amountLimit: BigDecimal,
        order: Order,
        totalPrice: BigDecimal,
        deliveryFee: BigDecimal
    ) {
        if (totalPrice < amountLimit) {
            order.price = totalPrice.add(deliveryFee)
            return
        }

        order.price = totalPrice
    }
}

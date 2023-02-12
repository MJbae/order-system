package com.order.application

import com.order.cli.dto.OrderResult
import com.order.domain.Item
import com.order.domain.OrderFactory
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
    private val orderFactory: OrderFactory,
) {

    fun order(orderCommands: List<OrderCommand>): OrderResult {
        var totalResult = OrderResult()
        val order = orderFactory.create(
            freeDeliveryThreshold = BigDecimal(50000),
            deliveryCharge = BigDecimal(2500)
        )

        for (orderCommand in orderCommands) {
            val item: Item = itemRepository.findByIdInLock(orderCommand.itemId)
            val newCommand = OrderCommand(orderCommand.itemId, orderCommand.orderQuantity, item)

            val orderResult = order.placeOrder(newCommand)

            totalResult = totalResult.add(orderResult)
            itemRepository.save(item)
        }

        orderRepository.save(order)

        return totalResult
    }
}

package com.order.application

import com.order.domain.Item
import com.order.domain.OrderFactory
import com.order.infra.ItemRepository
import com.order.infra.OrderRepository
import org.slf4j.LoggerFactory
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
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun order(orderCommands: List<OrderCommand>): OrderResult {
        val order = orderFactory.create(
            freeDeliveryThreshold = BigDecimal(50000),
            deliveryCharge = BigDecimal(2500)
        )

        var totalResult = OrderResult()

        try {
            for (orderCommand in orderCommands) {
                val item: Item = itemRepository.findByIdInLock(orderCommand.itemId)
                val newCommand = orderCommand.addItem(item)

                val orderResult = order.placeOrder(newCommand)

                totalResult = totalResult.add(orderResult)
                itemRepository.save(item)
            }

            orderRepository.save(order)
        } catch (e: Exception) {
            logger.info("Exception Message: ${e.message}")
        }

        return totalResult
    }
}

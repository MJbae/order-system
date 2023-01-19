package com.order.application

import com.order.cli.dto.OrderData
import com.order.cli.dto.OrderResult
import com.order.domain.Item
import com.order.domain.Order
import com.order.domain.PriceCalculator
import com.order.infra.ItemRepository
import com.order.infra.OrderRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class OrderService(
    private val orderRepository: OrderRepository,
    private val itemRepository: ItemRepository,
) {

    fun order(orderData: OrderData): OrderResult {
        val order = Order()
        val calculator = PriceCalculator()
        val item: Item = itemRepository.findByIdInLock(orderData.itemId)
        orderData.item = item

        val result = order.createWith(orderData, calculator)

        itemRepository.save(item)
        orderRepository.save(Order(null, result.price, result.orderItems.toMutableList()))

        return result
    }
}

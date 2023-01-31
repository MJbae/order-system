package com.order.application

import com.order.cli.dto.OrderData
import com.order.cli.dto.OrderResult
import com.order.domain.Item
import com.order.domain.OrderFactory
import com.order.domain.PriceCalculator
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

    fun order(orderData: OrderData): OrderResult {
        val order = orderFactory.create(
            freeDeliveryLimit = BigDecimal(50000),
            deliveryFee = BigDecimal(2500)
        )
        val calculator = PriceCalculator()
        val item: Item = itemRepository.findByIdInLock(orderData.itemId)
        orderData.item = item

        val result = order.placeOrder(orderData, calculator)

        itemRepository.save(item)
        orderRepository.save(order)

        return result
    }
}

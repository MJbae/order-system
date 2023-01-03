package com.order.application

import com.order.application.interfaces.OrderService
import com.order.cli.dto.OrderData
import com.order.domain.Customer
import com.order.domain.Item
import com.order.domain.Order
import com.order.infra.ItemRepository
import com.order.infra.OrderRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class OrderServiceImp(
    private val customer: Customer,
    private val orderRepository: OrderRepository,
    private val itemRepository: ItemRepository,
) : OrderService<Order, OrderData> {

    override fun order(orderData: OrderData): Order {
        val item: Item = itemRepository.findByIdInLock(orderData.itemId)
        orderData.item = item

        val order = customer.order(orderData)

        itemRepository.save(item)
        orderRepository.save(order)

        return order
    }
}

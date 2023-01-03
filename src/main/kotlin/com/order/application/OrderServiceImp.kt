package com.order.application

import com.order.application.interfaces.OrderService
import com.order.cli.dto.OrderData
import com.order.cli.dto.OrderResult
import com.order.domain.Customer
import com.order.domain.Item
import com.order.domain.Order
import com.order.infra.ItemRepository
import com.order.infra.OrderRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.transaction.Transactional

@Service
@Transactional
class OrderServiceImp(
    private val customer: Customer,
    private val orderRepository: OrderRepository,
    private val itemRepository: ItemRepository,
) : OrderService<OrderResult, OrderData> {

    override fun order(orderData: OrderData): OrderResult {
        val item: Item = itemRepository.findByIdInLock(orderData.itemId)
        orderData.item = item

        val result = customer.order(orderData)

        itemRepository.save(item)
        orderRepository.save(Order(null, result.price, result.orderItems.toMutableList()))

        return result
    }

    fun calculatePriceWith(totalPrice: BigDecimal, itemPrice: BigDecimal, orderQuantity: Int): BigDecimal {
        val priceSum = orderQuantity.times(itemPrice.toLong())
        return totalPrice + BigDecimal.valueOf(priceSum)
    }
}
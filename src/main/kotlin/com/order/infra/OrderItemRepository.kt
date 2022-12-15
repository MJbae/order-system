package com.order.infra

import com.order.domain.OrderItem
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository : JpaRepository<OrderItem, Long> {
    fun findAllByOrderId(orderId: Long): List<OrderItem>
}

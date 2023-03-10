package com.order.domain

import com.order.application.OrderCommand
import com.order.application.OrderResult
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "order_table")
class Order(
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Embedded
    var price: OrderPrice,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    var orderItems: MutableList<OrderItem>
) {
    fun placeOrder(orderCommand: OrderCommand): OrderResult {
        this.addOrderItem(orderCommand)

        this.price = price.calculatePriceTotal(orderCommand)
        val item = orderCommand.item ?: return OrderResult(false, price, arrayListOf())

        item.decreaseStock(orderCommand.orderQuantity)

        this.price = price.applyDeliveryCharge()

        return OrderResult(true, price, arrayListOf())
    }

    private fun addOrderItem(orderCommand: OrderCommand) {
        val orderItem = OrderItem(null, this, orderCommand.item!!, orderCommand.orderQuantity)
        this.orderItems.add(orderItem)
    }
}

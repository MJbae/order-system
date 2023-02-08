package com.order.domain

import com.order.cli.dto.OrderData
import com.order.cli.dto.OrderResult
import com.order.exception.SoldOutException
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
    fun placeOrder(orderData: OrderData): OrderResult {
        this.price = price.calculatePriceTotal(orderData)
        val item = orderData.item ?: return OrderResult(false, price, arrayListOf())

        try {
            item.decreaseStock(orderData.orderQuantity)
        } catch (e: SoldOutException) {
            return OrderResult(false, price, arrayListOf())
        }

        this.price = price.applyDeliveryCharge()

        return OrderResult(true, this.price, arrayListOf())
    }
}

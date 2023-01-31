package com.order.domain

import com.order.cli.dto.OrderData
import com.order.cli.dto.OrderResult
import com.order.exception.SoldOutException
import java.math.BigDecimal
import javax.persistence.CascadeType
import javax.persistence.Column
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

    @Column(name = "order_price")
    var price: BigDecimal?,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    var orderItems: MutableList<OrderItem>,

    @Transient
    val freeDeliveryLimit: BigDecimal,

    @Transient
    val deliveryFee: BigDecimal
) {

    fun placeOrder(orderData: OrderData): OrderResult {
        val orderPrice = calculateOrderPriceFrom(orderData)
        val item = orderData.item!!

        try {
            item.decreaseStock(orderData.orderQuantity)
        } catch (e: SoldOutException) {
            return OrderResult(false, orderPrice, arrayListOf())
        }

        this.addDeliveryFee(orderPrice)

        return OrderResult(true, this.price, arrayListOf())
    }

    private fun calculateOrderPriceFrom(orderData: OrderData): BigDecimal {
        return orderData.item?.price!! * BigDecimal(orderData.orderQuantity)
    }

    private fun addDeliveryFee(orderPrice: BigDecimal) {
        if (includesDeliveryFee(orderPrice)) {
            this.price = orderPrice + deliveryFee
            return
        }

        this.price = orderPrice
    }

    private fun includesDeliveryFee(totalPrice: BigDecimal): Boolean {
        if (totalPrice < freeDeliveryLimit) {
            return true
        }
        return false
    }
}

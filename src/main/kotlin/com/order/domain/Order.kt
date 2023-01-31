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
    val freeDeliveryThreshold: BigDecimal,

    @Transient
    val deliveryCharge: BigDecimal
) {

    fun placeOrder(orderData: OrderData): OrderResult {
        val orderPrice = calculateOrderPriceTotal(orderData)
        val item = orderData.item!!

        try {
            item.decreaseStock(orderData.orderQuantity)
        } catch (e: SoldOutException) {
            return OrderResult(false, orderPrice, arrayListOf())
        }

        this.applyDeliveryCharge(orderPrice)

        return OrderResult(true, this.price, arrayListOf())
    }

    private fun calculateOrderPriceTotal(orderData: OrderData): BigDecimal {
        return orderData.item?.price!! * BigDecimal(orderData.orderQuantity)
    }

    private fun applyDeliveryCharge(orderPrice: BigDecimal) {
        if (isDeliveryChargeRequired(orderPrice)) {
            this.price = orderPrice + deliveryCharge
            return
        }

        this.price = orderPrice
    }

    private fun isDeliveryChargeRequired(orderPrice: BigDecimal): Boolean {
        if (orderPrice < freeDeliveryThreshold) {
            return true
        }
        return false
    }
}

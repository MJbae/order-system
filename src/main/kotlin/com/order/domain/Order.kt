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
    var orderItems: MutableList<OrderItem>
) {

    constructor() : this(null, BigDecimal(0), arrayListOf())

    fun placeOrder(orderData: OrderData, calculator: PriceCalculator): OrderResult {
        val item = orderData.item
        var totalPrice = BigDecimal.ZERO

        totalPrice = calculator.totalPriceWith(totalPrice, item!!.price, orderData.orderQuantity)

        try {
            item.decreaseStock(orderData.orderQuantity)
        } catch (e: SoldOutException) {
            return OrderResult(false, totalPrice, arrayListOf())
        }

        this.updateOrderPriceBy(totalPrice)

        return OrderResult(true, this.price, arrayListOf())
    }

    private fun updateOrderPriceBy(totalPrice: BigDecimal) {

        val freeDeliveryLimit = BigDecimal(50000)
        val deliveryFee = BigDecimal(2500)

        if (totalPrice < freeDeliveryLimit) {
            this.price = totalPrice.add(deliveryFee)
            return
        }

        this.price = totalPrice
    }
}

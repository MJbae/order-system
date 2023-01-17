package com.order.domain

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
    fun addDeliveryFeeByAmountLimitTo(totalPrice: BigDecimal) {

        val freeDeliveryLimit = BigDecimal(50000)
        val deliveryFee = BigDecimal(2500)

        if (totalPrice < freeDeliveryLimit) {
            this.price = totalPrice.add(deliveryFee)
            return
        }

        this.price = totalPrice
    }

    constructor() : this(null, BigDecimal(0), arrayListOf())
}

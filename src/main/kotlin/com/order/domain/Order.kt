package com.order.domain

import com.order.cli.dto.OrderData
import com.order.cli.dto.OrderResult
import com.order.exception.SoldOutException
import org.slf4j.LoggerFactory
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
    @Transient
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun placeOrder(orderData: OrderData): OrderResult {
        val orderPrice = calculateOrderPriceTotal(orderData)
        val item = orderData.item ?: return OrderResult(false, BigDecimal.ZERO, arrayListOf())

        try {
            item.decreaseStock(orderData.orderQuantity)
        } catch (e: SoldOutException) {
            return OrderResult(false, orderPrice, arrayListOf())
        }

        this.applyDeliveryCharge(orderPrice)

        return OrderResult(true, this.price, arrayListOf())
    }

    private fun calculateOrderPriceTotal(orderData: OrderData): BigDecimal {
        return when (val itemPrice = orderData.item?.price) {
            null -> {
                logger.info("Calculation Error, Item price not found for orderData: $orderData")
                BigDecimal.ZERO
            }
            else -> {
                val orderQuantity = BigDecimal(orderData.orderQuantity)

                itemPrice * orderQuantity
            }
        }
    }

    private fun applyDeliveryCharge(orderPrice: BigDecimal) {
        this.price = if (isDeliveryChargeRequired(orderPrice)) orderPrice + deliveryCharge else orderPrice
    }

    private fun isDeliveryChargeRequired(orderPrice: BigDecimal): Boolean {
        return orderPrice < freeDeliveryThreshold
    }
}

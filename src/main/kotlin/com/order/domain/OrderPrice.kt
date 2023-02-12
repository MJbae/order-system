package com.order.domain

import com.order.application.OrderCommand
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import javax.persistence.Access
import javax.persistence.AccessType
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
@Access(AccessType.FIELD)
data class OrderPrice(
    @Column(name = "order_price")
    val value: BigDecimal,
    @Transient
    private val freeDeliveryThreshold: BigDecimal,
    @Transient
    private val deliveryCharge: BigDecimal
) {
    @Transient
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun calculatePriceTotal(orderCommand: OrderCommand): OrderPrice {
        return when (val itemPrice = orderCommand.item?.price) {
            null -> {
                logger.debug("Calculation Error, Item price not found for orderData: $orderCommand")
                OrderPrice(BigDecimal.ZERO, freeDeliveryThreshold, deliveryCharge)
            }

            else -> {
                val orderQuantity = BigDecimal(orderCommand.orderQuantity)
                OrderPrice(itemPrice * orderQuantity, freeDeliveryThreshold, deliveryCharge)
            }
        }
    }

    fun applyDeliveryCharge(): OrderPrice {
        if (isDeliveryChargeRequired(this.value)) {
            return OrderPrice(this.value + deliveryCharge, this.freeDeliveryThreshold, this.deliveryCharge)
        }

        return OrderPrice(this.value, this.freeDeliveryThreshold, this.deliveryCharge)
    }

    fun addValue(toAdd: OrderPrice): OrderPrice {
        return OrderPrice(this.value + toAdd.value, this.freeDeliveryThreshold, this.deliveryCharge)
    }

    private fun isDeliveryChargeRequired(value: BigDecimal): Boolean {
        return value < freeDeliveryThreshold
    }

    companion object {
        val ZERO = OrderPrice(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
    }
}

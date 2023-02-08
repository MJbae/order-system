package com.order.domain

import com.order.cli.dto.OrderData
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
    fun calculatePriceTotal(orderData: OrderData): OrderPrice {
        return when (val itemPrice = orderData.item?.price) {
            null -> {
                logger.info("Calculation Error, Item price not found for orderData: $orderData")
                OrderPrice(BigDecimal.ZERO, freeDeliveryThreshold, deliveryCharge)
            }

            else -> {
                val orderQuantity = BigDecimal(orderData.orderQuantity)
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

    private fun isDeliveryChargeRequired(value: BigDecimal): Boolean {
        return value < freeDeliveryThreshold
    }
}

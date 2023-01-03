package com.order.application

import com.order.cli.dto.OrderData
import com.order.domain.Customer
import com.order.domain.Item
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Transactional
internal class OrderUnitTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val itemPrice: BigDecimal = BigDecimal.valueOf(45000)
    val deliveryFee: BigDecimal = BigDecimal.valueOf(2500)
    val stockQuantity = 7
    val itemName = "캠핑덕 우드롤테이블"
    val itemId = 778422L
    var orderQuantity = 0

    describe("주문 시") {
        context("주문금액이 5만원 이상이라면") {
            this.beforeTest {
                orderQuantity = 2
            }
            it("주문금액에 배송료를 포함하지 않는다") {
                val sut = Customer()
                val item = Item(itemId, itemPrice, itemName, stockQuantity)
                val orderData = OrderData(itemId, orderQuantity, item)
                val expectedPrice = itemPrice.multiply(BigDecimal.valueOf(orderQuantity.toLong()))

                val order = sut.order(orderData)

                order.price shouldBe expectedPrice
            }
        }
        context("주문금액이 5만원 미만이라면") {
            this.beforeTest {
                orderQuantity = 1
            }
            it("주문금액에 배송료를 포함한다") {
                val sut = Customer()
                val item = Item(itemId, itemPrice, itemName, stockQuantity)
                val orderData = OrderData(itemId, orderQuantity, item)
                val expectedPrice = itemPrice.multiply(BigDecimal.valueOf(orderQuantity.toLong())).add(deliveryFee)

                val order = sut.order(orderData)

                order.price shouldBe expectedPrice
            }
        }
    }
})

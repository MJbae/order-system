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

    val itemName = "캠핑덕 우드롤테이블"
    val itemId = 778422L
    var itemPrice: BigDecimal = BigDecimal.ZERO
    var deliveryFee: BigDecimal = BigDecimal.ZERO
    var orderQuantity = 0
    var stockQuantity = 0

    describe("주문 시") {
        context("주문금액이 5만원 이상이라면") {
            orderQuantity = 2
            stockQuantity = 7
            itemPrice = BigDecimal.valueOf(45000)

            val sut = Customer()
            val item = Item(itemId, itemPrice, itemName, stockQuantity)
            val orderData = OrderData(itemId, orderQuantity, item)
            val expectedPrice = itemPrice.multiply(BigDecimal.valueOf(orderQuantity.toLong()))

            it("주문금액에 배송료를 포함하지 않는다") {
                val result = sut.order(orderData)

                result.price shouldBe expectedPrice
            }
            it("주문이 성공한다") {
                val result = sut.order(orderData)

                result.isSuccess shouldBe true
            }
        }
        context("주문금액이 5만원 미만이라면") {
            orderQuantity = 1
            stockQuantity = 7
            itemPrice = BigDecimal.valueOf(45000)
            deliveryFee = BigDecimal.valueOf(2500)

            val sut = Customer()
            val item = Item(itemId, itemPrice, itemName, stockQuantity)
            val orderData = OrderData(itemId, orderQuantity, item)
            val expectedPrice = itemPrice.multiply(BigDecimal.valueOf(orderQuantity.toLong())).add(deliveryFee)

            it("주문금액에 배송료를 포함한다") {
                val result = sut.order(orderData)

                result.price shouldBe expectedPrice
            }
            it("주문이 성공한다") {
                val result = sut.order(orderData)

                result.isSuccess shouldBe true
            }
        }
        context("상품의 재고 보다 많은 수량이 주문된다면") {
            orderQuantity = 10
            stockQuantity = 7
            itemPrice = BigDecimal.valueOf(45000)

            val sut = Customer()
            val item = Item(itemId, itemPrice, itemName, stockQuantity)
            val orderData = OrderData(itemId, orderQuantity, item)

            it("주문이 실패한다") {
                val result = sut.order(orderData)

                result.isSuccess shouldBe false
            }
        }
    }
})

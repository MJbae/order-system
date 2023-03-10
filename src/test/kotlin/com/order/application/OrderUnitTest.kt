package com.order.application

import com.order.domain.Item
import com.order.domain.OrderFactory
import com.order.domain.Stock
import com.order.exception.SoldOutException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.math.BigDecimal

internal class OrderUnitTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    val orderFactory = OrderFactory()

    describe("주문 시") {
        context("주문금액이 5만원 이상이라면") {

            val orderData = createOrderData(
                itemPrice = BigDecimal.valueOf(45000),
                itemName = "캠핑덕 우드롤테이블", Stock(7), orderQuantity = 2
            )
            val sut = orderFactory.create(
                freeDeliveryThreshold = BigDecimal(50000),
                deliveryCharge = BigDecimal(2500)
            )

            val result = sut.placeOrder(orderData)

            it("주문금액에 배송료를 포함하지 않는다") {
                result.price.value shouldBe BigDecimal.valueOf(90000)
            }
            it("주문이 성공한다") {
                result.isSuccess shouldBe true
            }
        }
        context("주문금액이 5만원 미만이라면") {
            val orderData = createOrderData(
                itemPrice = BigDecimal.valueOf(45000),
                itemName = "캠핑덕 우드롤테이블", stock = Stock(7), orderQuantity = 1
            )
            val sut = orderFactory.create(
                freeDeliveryThreshold = BigDecimal(50000),
                deliveryCharge = BigDecimal(2500)
            )

            val result = sut.placeOrder(orderData)

            it("주문금액에 배송료를 포함한다") {
                result.price.value shouldBe BigDecimal.valueOf(47500)
            }
            it("주문이 성공한다") {
                result.isSuccess shouldBe true
            }
        }
        context("상품의 재고 보다 많은 수량이 주문된다면") {
            val orderData = createOrderData(
                itemPrice = BigDecimal.valueOf(45000),
                itemName = "캠핑덕 우드롤테이블", stock = Stock(7), orderQuantity = 10
            )
            val sut = orderFactory.create(
                freeDeliveryThreshold = BigDecimal(50000),
                deliveryCharge = BigDecimal(2500)
            )

            it("주문이 실패한다") {
                shouldThrow<SoldOutException> { sut.placeOrder(orderData) }
            }
        }
    }
})

const val ITEM_ID = 778422L
private fun createOrderData(
    itemPrice: BigDecimal,
    itemName: String,
    stock: Stock,
    orderQuantity: Int
): OrderCommand {
    val item = Item(itemPrice, itemName, stock)
    return OrderCommand(ITEM_ID, orderQuantity, item)
}

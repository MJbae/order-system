package com.order.application

import com.order.cli.dto.OrderData
import com.order.domain.Item
import com.order.domain.Order
import com.order.infra.ItemRepository
import com.order.infra.OrderRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.math.BigDecimal

@DataJpaTest
class OrderIntegrationTest(
    @Autowired private val itemRepository: ItemRepository,
    @Autowired private val orderRepository: OrderRepository
) : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension)

    private val order: Order = Order()

    init {
        this.describe("주문 시") {
            context("주문금액이 5만원 이상이라면") {
                it("주문금액에 배송료를 포함하지 않는다") {
                    val orderData = createOrderData(
                        itemPrice = BigDecimal.valueOf(45000),
                        itemName = "캠핑덕 우드롤테이블", stockQuantity = 7, orderQuantity = 2
                    )
                    sut = OrderService(order, orderRepository, itemRepository)

                    val result = sut.order(orderData)

                    result.price shouldBe BigDecimal.valueOf(90000)
                    result.isSuccess shouldBe true
                }
            }
        }
    }

    companion object {
        private lateinit var sut: OrderService
    }

    val itemId = 778422L
    private fun createOrderData(
        itemPrice: BigDecimal,
        itemName: String,
        stockQuantity: Int,
        orderQuantity: Int
    ): OrderData {
        val item = Item(this.itemId, itemPrice, itemName, stockQuantity)
        return OrderData(this.itemId, orderQuantity, item)
    }
}

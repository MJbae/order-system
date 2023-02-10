package com.order.application

import com.order.cli.dto.OrderData
import com.order.domain.Item
import com.order.domain.OrderFactory
import com.order.domain.Stock
import com.order.infra.ItemRepository
import com.order.infra.OrderRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest
class OrderIntegrationTest(
    @Autowired private val itemRepository: ItemRepository,
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val orderFactory: OrderFactory
) : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension)

    init {
        this.describe("주문 시") {
            context("주문금액이 5만원 이상이라면") {
                it("주문금액에 배송료를 포함하지 않는다") {
                    val orderData = createOrderData(
                        itemPrice = BigDecimal.valueOf(45000),
                        itemName = "캠핑덕 우드롤테이블", stock = Stock(7), orderQuantity = 2
                    )
                    sut = OrderService(orderRepository, itemRepository, orderFactory)

                    val result = sut.order(orderData)

                    result.price?.value shouldBe BigDecimal.valueOf(90000)
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
        stock: Stock,
        orderQuantity: Int
    ): OrderData {
        val item = Item(itemPrice, itemName, stock)
        return OrderData(this.itemId, orderQuantity, item)
    }
}

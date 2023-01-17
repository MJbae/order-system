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

    private var orderQuantity: Int = 0
    private var itemPrice: BigDecimal = BigDecimal.valueOf(45000)
    private var stockQuantity: Int = 7
    private val itemId: Long = 778422L
    private val order: Order = Order()

    init {
        this.describe("주문 시") {
            context("주문금액이 5만원 이상이라면") {
                orderQuantity = 2
                stockQuantity = 7
                itemPrice = BigDecimal.valueOf(45000)
                val itemName = "캠핑덕 우드롤테이블"
                val item = Item(itemId, itemPrice, itemName, stockQuantity)
                it("주문금액에 배송료를 포함하지 않는다") {
                    sut = OrderService(order, orderRepository, itemRepository)
                    orderData = OrderData(itemId, orderQuantity, item)
                    val result = sut.order(orderData)

                    result.price shouldBe itemPrice.multiply(BigDecimal(orderQuantity))
                    result.isSuccess shouldBe true
                }
            }
        }
    }

    companion object {
        private lateinit var sut: OrderService
        private lateinit var orderData: OrderData
    }
}

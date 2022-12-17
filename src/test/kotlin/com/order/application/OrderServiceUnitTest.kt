package com.order.application

import com.order.cli.dto.OrderData
import com.order.domain.Item
import com.order.domain.Order
import com.order.domain.OrderItem
import com.order.infra.ItemRepository
import com.order.infra.OrderItemRepository
import com.order.infra.OrderRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Transactional
internal class OrderServiceUnitTest {
    private var service: OrderService? = null
    private val orderRepository = Mockito.mock(OrderRepository::class.java)
    private val orderItemRepository = Mockito.mock(OrderItemRepository::class.java)
    private val itemRepository = Mockito.mock(ItemRepository::class.java)
    private var orderMocking: Order? = null
    private var item: Item? = null
    private var orderItem: OrderItem? = null
    private var orderCount = 0
    private var orderData: MutableList<OrderData>? = null
    private var stockQuantity = 0
    private val itemId = 778422L
    private val itemName = "캠핑덕 우드롤테이블"
    private val itemPrice = BigDecimal.valueOf(45000)

    @BeforeEach
    fun setUp() {
        orderData = ArrayList()
        service = OrderService(orderRepository, itemRepository, orderItemRepository)
    }

    @Nested
    internal inner class `order 메소드는` {
        @Nested
        internal inner class `만약 주문금액이 5만원 이상이면` {
            @BeforeEach
            fun setUp() {
                orderCount = 2
                stockQuantity = 3
                orderMocking = Order(1L, BigDecimal(0))
                item = Item(itemId, itemPrice, itemName, stockQuantity)
                orderItem = OrderItem(orderMocking!!, item!!, orderCount)

                BDDMockito.given(itemRepository.findByIdInLock(itemId)).willReturn(item)
            }

            @Test
            fun `주문금액에 배송료를 포함하지 않고 반환한다`() {
                orderData!!.add(OrderData(itemId, orderCount))

                val order = service!!.order(orderData!!)
                val actualPrice = order.price
                val expectedPrice = itemPrice.multiply(BigDecimal.valueOf(orderCount.toLong()))

                Assertions.assertThat(actualPrice).isEqualTo(expectedPrice)
            }
        }
    }
}

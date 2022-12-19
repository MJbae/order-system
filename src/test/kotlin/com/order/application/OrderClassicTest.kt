package com.order.application

import com.order.cli.dto.OrderData
import com.order.domain.Item
import com.order.domain.Order
import com.order.domain.OrderItem
import com.order.exception.SoldOutException
import com.order.infra.ItemRepository
import com.order.infra.OrderItemRepository
import com.order.infra.OrderRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@SpringBootTest
@Transactional
internal class OrderClassicTest {
    @Autowired
    private lateinit var service: OrderService

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var orderItemRepository: OrderItemRepository

    @Autowired
    private lateinit var itemRepository: ItemRepository

    private var order: Order? = null
    private var item: Item? = null
    private var orderItem: OrderItem? = null
    private var orderQuantity = 0
    private var orderData: MutableList<OrderData>? = null
    private val itemPrice: BigDecimal = BigDecimal.valueOf(45000)
    private val stockQuantity = 7
    private val itemId = 778422L
    private val itemName = "캠핑덕 우드롤테이블"
    private val deliveryFee: BigDecimal = BigDecimal.valueOf(2500)

    @BeforeEach
    fun setUp() {
        orderData = ArrayList()
        service = OrderService(orderRepository, itemRepository, orderItemRepository)

        order = Order(1L, BigDecimal(0))
        item = Item(itemId, itemPrice, itemName, stockQuantity)
        orderItem = OrderItem(order!!, item!!, orderQuantity)
    }

    @Nested
    internal inner class `order 메소드는` {
        @Nested
        internal inner class `만약 주문금액이 5만원 이상이면` {
            @BeforeEach
            fun setUp() {
                orderQuantity = 2
            }

            @Test
            fun `주문금액에 배송료를 포함하지 않고 반환한다`() {
                orderData!!.add(OrderData(itemId, orderQuantity))

                val order = service.order(orderData!!)
                val actualPrice = order.price
                val expectedPrice = itemPrice.multiply(BigDecimal.valueOf(orderQuantity.toLong()))

                Assertions.assertThat(actualPrice).isEqualTo(expectedPrice)
            }
        }

        @Nested
        internal inner class `만약 주문금액이 5만원 미만이면` {
            @BeforeEach
            fun setUp() {
                orderQuantity = 1
            }

            @Test
            fun `주문금액에 배송료를 포함하고 반환한다`() {
                orderData!!.add(OrderData(itemId, orderQuantity))

                val order = service.order(orderData!!)
                val actualPrice = order.price
                val expectedPrice = itemPrice
                    .multiply(BigDecimal.valueOf(orderQuantity.toLong()))
                    .add(deliveryFee)

                Assertions.assertThat(actualPrice).isEqualTo(expectedPrice)
            }
        }

        @Nested
        internal inner class `만약 하나의 주문에 두 건 이상의 상품주문 내역이 있다면` {
            @BeforeEach
            fun setUp() {
                orderQuantity = 1
            }

            @Test
            fun `상품주문 내역 금액의 합을 주문금액에 반영하여 반환한다`() {
                orderData!!.add(OrderData(itemId, orderQuantity))
                orderData!!.add(OrderData(itemId, orderQuantity))

                val order = service.order(orderData!!)
                val priceSum = itemPrice
                    .multiply(BigDecimal.valueOf(orderQuantity.toLong()))

                val actualPrice = order.price
                val expectedPrice = priceSum + priceSum

                Assertions.assertThat(actualPrice).isEqualTo(expectedPrice)
            }
        }

        @Nested
        internal inner class `만약 상품의 재고 보다 많은 수량을 주문한다면` {
            @BeforeEach
            fun setUp() {
                orderQuantity = 10 // stockQuantity = 7
            }

            @Test
            fun `재고부족 예외를 발생시킨다`() {
                orderData!!.add(OrderData(itemId, orderQuantity))

                assertThrows<SoldOutException> { service.order(orderData!!) }
            }
        }

        @Nested
        internal inner class `만약 두 건 이상의 상품주문 중 재고 보다 많은 상품을 주문한다면` {
            @BeforeEach
            fun setUp() {
                orderQuantity = 5 // stockQuantity = 7
            }

            @Test
            fun `재고부족 예외를 발생시킨다`() {
                orderData!!.add(OrderData(itemId, orderQuantity))
                orderData!!.add(OrderData(itemId, orderQuantity))

                assertThrows<SoldOutException> { service.order(orderData!!) }
            }
        }
    }
}

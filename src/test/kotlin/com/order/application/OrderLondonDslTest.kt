package com.order.application

import com.order.cli.dto.OrderData
import com.order.domain.Item
import com.order.domain.Order
import com.order.domain.OrderItem
import com.order.exception.SoldOutException
import com.order.infra.ItemRepository
import com.order.infra.OrderItemRepository
import com.order.infra.OrderRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Transactional
internal class OrderLondonDslTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val orderRepository = mockk<OrderRepository>()
    val orderItemRepository = mockk<OrderItemRepository>()
    val itemRepository = mockk<ItemRepository>()
    val service = OrderService(orderRepository, itemRepository)

    var orderQuantity = 0
    val orderData: ArrayList<OrderData> = ArrayList()

    val itemPrice: BigDecimal = BigDecimal.valueOf(45000)
    val deliveryFee: BigDecimal = BigDecimal.valueOf(2500)
    val stockQuantity = 7
    val itemId = 778422L
    val itemName = "캠핑덕 우드롤테이블"
    val item = Item(itemId, itemPrice, itemName, stockQuantity)

    beforeTest {
        orderData.clear()

        val orderPrice = BigDecimal.valueOf(90000)
        val order = Order(null, orderPrice, arrayListOf())

        every { itemRepository.findByIdInLock(itemId) } returns item
        every { itemRepository.save(any()) } returns item
        every { orderRepository.save(any()) } returns order
        every { orderItemRepository.save(any()) } returns OrderItem(null, order, item, orderQuantity)
    }

    describe("주문 시") {
        context("주문금액이 5만원 이상이라면") {
            beforeTest {
                orderQuantity = 2
            }
            it("주문금액에 배송료를 포함하지 않는다") {
                orderData.add(OrderData(itemId, orderQuantity))
                val order = service.order(orderData)

                val actualPrice = order.price
                val expectedPrice = itemPrice.multiply(BigDecimal.valueOf(orderQuantity.toLong()))

                actualPrice shouldBe expectedPrice
                verify(exactly = 1) { itemRepository.findByIdInLock(itemId) }
            }
        }
        context("주문금액이 5만원 미만이라면") {
            beforeTest {
                orderQuantity = 1
            }
            it("주문금액에 배송료를 포함한다") {
                orderData.add(OrderData(itemId, orderQuantity))
                val order = service.order(orderData)

                val actualPrice = order.price
                val expectedPrice = itemPrice
                    .multiply(BigDecimal.valueOf(orderQuantity.toLong()))
                    .add(deliveryFee)

                actualPrice shouldBe expectedPrice
                verify(exactly = 1) { itemRepository.findByIdInLock(itemId) }
            }
        }
        context("두 건 이상의 상품주문이 포함된다면") {
            beforeTest {
                orderQuantity = 1
            }
            it("주문금액에 해당 상품 금액의 합이 반영된다") {
                orderData.add(OrderData(itemId, orderQuantity))
                orderData.add(OrderData(itemId, orderQuantity))
                val order = service.order(orderData)

                val priceSum = itemPrice.multiply(BigDecimal.valueOf(orderQuantity.toLong()))
                val actualPrice = order.price
                val expectedPrice = priceSum + priceSum

                actualPrice shouldBe expectedPrice
                verify(exactly = 2) { itemRepository.findByIdInLock(itemId) }
            }
        }
        context("상품의 재고 보다 많은 수량이 주문된다면") {
            beforeTest {
                orderQuantity = 10 // stockQuantity = 7
            }
            it("주문이 실패한다") {
                orderData.add(OrderData(itemId, orderQuantity))

                shouldThrow<SoldOutException> { service.order(orderData) }
                verify(exactly = 1) { itemRepository.findByIdInLock(itemId) }
            }
        }
        context("두 건 이상의 상품주문 중 재고 보다 많은 상품이 주문된다면") {
            beforeTest {
                orderQuantity = 5 // stockQuantity = 7
            }
            it("재고부족 예외를 발생시킨다") {
                orderData.add(OrderData(itemId, orderQuantity))
                orderData.add(OrderData(itemId, orderQuantity))

                shouldThrow<SoldOutException> { service.order(orderData) }
                verify(exactly = 2) { itemRepository.findByIdInLock(itemId) }
            }
        }
    }
})

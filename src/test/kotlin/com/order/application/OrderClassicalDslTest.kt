package com.order.application

import com.order.cli.dto.OrderData
import com.order.exception.SoldOutException
import com.order.infra.ItemRepository
import com.order.infra.OrderItemRepository
import com.order.infra.OrderRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.math.BigDecimal

@DataJpaTest
class OrderClassicalDslTest(
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val orderItemRepository: OrderItemRepository,
    @Autowired private val itemRepository: ItemRepository,
) : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension)

    private var orderQuantity: Int = 0
    private val orderData: ArrayList<OrderData> = ArrayList()

    private val itemPrice: BigDecimal = BigDecimal.valueOf(45000)
    private val deliveryFee: BigDecimal = BigDecimal.valueOf(2500)
    private val stockQuantity: Int = 7
    private val itemId: Long = 778422L

    init {
        this.beforeTest {
            orderData.clear()
            service = OrderService(orderRepository, itemRepository)
        }
        this.describe("order 메소드는") {
            context("만약 주문금액이 5만원 이상이면") {
                this.beforeTest {
                    orderQuantity = 2
                }
                it("주문금액에 배송료를 포함하지 않고 반환한다") {
                    orderData.add(OrderData(itemId, orderQuantity))
                    val order = service.order(orderData)

                    val actualPrice = order.price
                    val expectedPrice = itemPrice.multiply(BigDecimal.valueOf(orderQuantity.toLong()))
                    val actualStockQuantity = itemRepository.findByIdInLock(itemId).stockQuantity
                    val expectedStockQuantity = stockQuantity - orderQuantity

                    actualPrice shouldBe expectedPrice
                    actualStockQuantity shouldBe expectedStockQuantity
                }
            }
            context("만약 주문금액이 5만원 미만이면") {
                this.beforeTest {
                    orderQuantity = 1
                }
                it("주문금액에 배송료를 포함하고 반환한다") {
                    orderData.add(OrderData(itemId, orderQuantity))
                    val order = service.order(orderData)

                    val actualPrice = order.price
                    val expectedPrice = itemPrice
                        .multiply(BigDecimal.valueOf(orderQuantity.toLong()))
                        .add(deliveryFee)
                    val actualStockQuantity = itemRepository.findByIdInLock(itemId).stockQuantity
                    val expectedStockQuantity = stockQuantity - orderQuantity

                    actualPrice shouldBe expectedPrice
                    actualStockQuantity shouldBe expectedStockQuantity
                }
            }
            context("만약 하나의 주문에 두 건 이상의 상품주문 내역이 있다면") {
                this.beforeTest {
                    orderQuantity = 1
                }
                it("상품주문 내역 금액의 합을 주문금액에 반영하여 반환한다") {
                    orderData.add(OrderData(itemId, orderQuantity))
                    orderData.add(OrderData(itemId, orderQuantity))
                    val order = service.order(orderData)

                    val priceSum = itemPrice.multiply(BigDecimal.valueOf(orderQuantity.toLong()))
                    val actualPrice = order.price
                    val expectedPrice = priceSum + priceSum
                    val actualStockQuantity = itemRepository.findByIdInLock(itemId).stockQuantity
                    val expectedStockQuantity = stockQuantity - orderQuantity - orderQuantity

                    actualPrice shouldBe expectedPrice
                    actualStockQuantity shouldBe expectedStockQuantity
                }
            }
            context("만약 상품의 재고 보다 많은 수량을 주문한다면") {
                this.beforeTest {
                    orderQuantity = 10 // stockQuantity = 7
                }
                it("재고부족 예외를 발생시킨다") {
                    orderData.add(OrderData(itemId, orderQuantity))

                    val actualStockQuantity = itemRepository.findByIdInLock(itemId).stockQuantity
                    val expectedStockQuantity = stockQuantity

                    shouldThrow<SoldOutException> { service.order(orderData) }
                    actualStockQuantity shouldBe expectedStockQuantity
                }
            }
            context("만약 두 건 이상의 상품주문 중 재고 보다 많은 상품을 주문한다면") {
                this.beforeTest {
                    orderQuantity = 5 // stockQuantity = 7
                }
                it("재고부족 예외를 발생시킨다") {
                    orderData.add(OrderData(itemId, orderQuantity))
                    orderData.add(OrderData(itemId, orderQuantity))

                    val actualStockQuantity = itemRepository.findByIdInLock(itemId).stockQuantity
                    val expectedStockQuantity = stockQuantity

                    shouldThrow<SoldOutException> { service.order(orderData) }
                    actualStockQuantity shouldBe expectedStockQuantity
                }
            }
        }
    }

    companion object {
        private lateinit var service: OrderService
    }
}

// package com.order.application
//
// import com.order.cli.dto.OrderData
// import com.order.exception.SoldOutException
// import com.order.infra.ItemRepository
// import com.order.infra.OrderRepository
// import io.kotest.assertions.throwables.shouldThrow
// import io.kotest.core.spec.style.DescribeSpec
// import io.kotest.extensions.spring.SpringExtension
// import io.kotest.matchers.shouldBe
// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
// import java.math.BigDecimal
//
// @DataJpaTest
// class OrderClassicalDslTest(
//    @Autowired private val orderRepository: OrderRepository,
//    @Autowired private val itemRepository: ItemRepository,
// ) : DescribeSpec() {
//    override fun extensions() = listOf(SpringExtension)
//
//    private var orderQuantity: Int = 0
//    private val orderData: ArrayList<OrderData> = ArrayList()
//
//    private val itemPrice: BigDecimal = BigDecimal.valueOf(45000)
//    private val deliveryFee: BigDecimal = BigDecimal.valueOf(2500)
//    private val stockQuantity: Int = 7
//    private val itemId: Long = 778422L
//
//    init {
//        this.beforeTest {
//            orderData.clear()
//            sut = OrderService(orderRepository, itemRepository)
//        }
//        this.describe("주문 시") {
//            context("주문금액이 5만원 이상이라면") {
//                this.beforeTest {
//                    orderQuantity = 2
//                }
//                it("주문금액에 배송료를 포함하지 않는다") {
//                    orderData.add(OrderData(itemId, orderQuantity))
//
//                    val order = sut.order(orderData)
//                    val actualPrice = order.price
//                    val expectedPrice = itemPrice.multiply(BigDecimal.valueOf(orderQuantity.toLong()))
//                    val actualStockQuantity = itemRepository.findByIdInLock(itemId).stockQuantity
//                    val expectedStockQuantity = stockQuantity - orderQuantity
//
//                    actualPrice shouldBe expectedPrice
//                    actualStockQuantity shouldBe expectedStockQuantity
//                }
//            }
//            context("주문금액이 5만원 미만이라면") {
//                this.beforeTest {
//                    orderQuantity = 1
//                }
//                it("주문금액에 배송료를 포함한다") {
//                    orderData.add(OrderData(itemId, orderQuantity))
//
//                    val order = sut.order(orderData)
//                    val actualPrice = order.price
//                    val expectedPrice = itemPrice
//                        .multiply(BigDecimal.valueOf(orderQuantity.toLong()))
//                        .add(deliveryFee)
//                    val actualStockQuantity = itemRepository.findByIdInLock(itemId).stockQuantity
//                    val expectedStockQuantity = stockQuantity - orderQuantity
//
//                    actualPrice shouldBe expectedPrice
//                    actualStockQuantity shouldBe expectedStockQuantity
//                }
//            }
//            context("두 건 이상의 상품주문이 포함된다면") {
//                this.beforeTest {
//                    orderQuantity = 1
//                }
//                it("주문금액에 해당 상품 금액의 합이 반영된다") {
//                    orderData.add(OrderData(itemId, orderQuantity))
//                    orderData.add(OrderData(itemId, orderQuantity))
//
//                    val order = sut.order(orderData)
//                    val priceSum = itemPrice.multiply(BigDecimal.valueOf(orderQuantity.toLong()))
//                    val actualPrice = order.price
//                    val expectedPrice = priceSum + priceSum
//                    val actualStockQuantity = itemRepository.findByIdInLock(itemId).stockQuantity
//                    val expectedStockQuantity = stockQuantity - orderQuantity - orderQuantity
//
//                    actualPrice shouldBe expectedPrice
//                    actualStockQuantity shouldBe expectedStockQuantity
//                }
//            }
//            context("상품의 재고 보다 많은 수량이 주문된다면") {
//                this.beforeTest {
//                    orderQuantity = 10 // stockQuantity = 7
//                }
//                it("주문이 실패한다") {
//                    orderData.add(OrderData(itemId, orderQuantity))
//
//                    val actualStockQuantity = itemRepository.findByIdInLock(itemId).stockQuantity
//                    val expectedStockQuantity = stockQuantity
//
//                    shouldThrow<SoldOutException> { sut.order(orderData) }
//                    actualStockQuantity shouldBe expectedStockQuantity
//                }
//            }
//            context("두 건 이상의 상품주문 중 재고 보다 많은 상품이 주문된다면") {
//                this.beforeTest {
//                    orderQuantity = 5 // stockQuantity = 7
//                }
//                it("주문이 실패한다") {
//                    orderData.add(OrderData(itemId, orderQuantity))
//                    orderData.add(OrderData(itemId, orderQuantity))
//
//                    val actualStockQuantity = itemRepository.findByIdInLock(itemId).stockQuantity
//                    val expectedStockQuantity = stockQuantity
//
//                    shouldThrow<SoldOutException> { sut.order(orderData) }
//                    actualStockQuantity shouldBe expectedStockQuantity
//                }
//            }
//        }
//    }
//
//    companion object {
//        private lateinit var sut: OrderService
//    }
// }

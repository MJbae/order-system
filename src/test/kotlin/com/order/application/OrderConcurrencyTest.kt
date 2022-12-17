package com.order.application

import com.order.cli.dto.OrderData
import com.order.domain.Item
import com.order.exception.SoldOutException
import com.order.infra.ItemRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
class OrderConcurrencyTest {
    @Autowired
    private val service: OrderService? = null

    @Autowired
    private val itemRepository: ItemRepository? = null

    private val orderData: MutableList<OrderData> = ArrayList()
    private val itemId = 778422L
    private val orderQuantity = 1
    private var stockQuantity = 7

    @BeforeEach
    fun setUp() {
        val item: Item = itemRepository!!.findByIdInLock(itemId)
        stockQuantity = item.stockQuantity
        orderData.add(OrderData(itemId, orderQuantity))
    }

    @Test
    @Throws(InterruptedException::class)
    fun `동시적으로 복수의 주문이 들어온다면 재고수만큼만 주문에 성공하고 나머지 주문에서는 재고부족 예외가 발생한다`() {
        val successCount = AtomicInteger()
        val numberOfExecution = 50
        val executorService: ExecutorService = Executors.newFixedThreadPool(5)
        val countDownLatch = CountDownLatch(numberOfExecution)

        for (i in 1..numberOfExecution) {
            executorService.execute {
                try {
                    service!!.order(orderData)
                    successCount.getAndIncrement()
                    println("주문 성공")
                } catch (soldOutException: SoldOutException) {
                    println("재고 부족 예외 발생")
                } catch (e: Exception) {
                    println(e.message)
                }
                countDownLatch.countDown()
            }
        }

        countDownLatch.await()
        assertThat(successCount.get()).isEqualTo(stockQuantity)
    }
}

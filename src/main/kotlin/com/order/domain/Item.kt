package com.order.domain

import com.order.exception.SoldOutException
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version

@Entity
@Table(name = "item")
class Item(
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long?,

    val price: BigDecimal,

    @Column(name = "item_name")
    val name: String,

    var stockQuantity: Int,

    @Version
    private var version: Int?
) {
    constructor(price: BigDecimal, name: String, stockQuantity: Int) : this(
        null,
        price = price,
        name = name,
        stockQuantity = stockQuantity,
        version = null
    )

    fun decreaseStock(orderQuantity: Int) {
        val soldOutMessage = "주문한 상품의 수가 재고량 보다 많습니다."

        if (orderQuantity > this.stockQuantity) {
            throw SoldOutException(soldOutMessage)
        }

        stockQuantity -= orderQuantity
    }

    override fun toString(): String {
        return "${this.id}    ${this.name}          ${this.price}원          ${this.stockQuantity}개"
    }
}

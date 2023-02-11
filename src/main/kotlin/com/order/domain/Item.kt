package com.order.domain

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Embedded
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
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long?,

    val price: BigDecimal,

    @Column(name = "item_name")
    val name: String,

    @Embedded
    var stock: Stock,

    @Version
    private var version: Int?
) {
    constructor(price: BigDecimal, name: String, stock: Stock) : this(
        id = null,
        price = price,
        name = name,
        stock = stock,
        version = null
    )

    fun decreaseStock(orderQuantity: Int) {
        this.stock = stock.decrease(orderQuantity)
    }

    fun showItem(): String {
        return "${this.id}    ${this.name}          ${this.price}원             ${this.stock.quantity}개"
    }
}

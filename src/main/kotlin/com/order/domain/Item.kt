package com.order.domain

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

    val stockQuantity: Int,

    @Version
    private var version: Int?
) {
    constructor(id: Long?, price: BigDecimal, name: String, stockQuantity: Int) : this(
        null,
        price = price,
        name = name,
        stockQuantity = stockQuantity,
        version = null
    )

    override fun toString(): String {
        return "${this.id}    ${this.name}          ${this.price}원          ${this.stockQuantity}개"
    }
}

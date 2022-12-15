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

    private val price: BigDecimal,

    @Column(name = "item_name")
    private val name: String,

    private val stockQuantity: Int,

    @Version
    private var version: Int
) {
    override fun toString(): String {
        return "${this.id}    ${this.name}          ${this.price}원          ${this.stockQuantity}개"
    }
}

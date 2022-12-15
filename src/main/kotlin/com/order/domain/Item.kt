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
    var id: Long?,

    val price: BigDecimal,

    @Column(name = "item_name")
    val name: String,

    val stockQuantity: Int,

    @Version
    val version: Int
) {
    override fun toString(): String {
        return "${this.id}    ${this.name}          ${this.price}원          ${this.stockQuantity}개"
    }
}

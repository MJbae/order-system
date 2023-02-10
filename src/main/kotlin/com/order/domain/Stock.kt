package com.order.domain

import com.order.exception.SoldOutException
import javax.persistence.Access
import javax.persistence.AccessType
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
@Access(AccessType.FIELD)
data class Stock(
    @Column(name = "stock_quantity")
    val quantity: Int
) {

    fun decrease(toMinus: Int): Stock {
        if (toMinus > quantity) {
            throw SoldOutException("주문한 상품의 수가 재고량 보다 많습니다.")
        }

        return Stock(this.quantity - toMinus)
    }
}

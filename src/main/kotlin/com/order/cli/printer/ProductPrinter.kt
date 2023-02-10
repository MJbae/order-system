package com.order.cli.printer

import com.order.cli.interfaces.Printer
import com.order.infra.ItemRepository
import org.springframework.stereotype.Component

@Component
class ProductPrinter(
    private val itemRepository: ItemRepository
) : Printer {
    private var categoryMessage = "상품번호                 상품명               판매가격                재고수"
    override fun show() {
        println(this.categoryMessage)
        itemRepository.findAll().forEach { item ->
            println(item.showItem())
        }
    }
}

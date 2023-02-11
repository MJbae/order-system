package com.order.cli.printer

import com.order.application.ItemService
import com.order.cli.interfaces.Printer
import org.springframework.stereotype.Component

@Component
class ItemPrinter(
    private val itemService: ItemService
) : Printer {
    private val categoryMessage = "상품번호                 상품명               판매가격                재고수"
    override fun show() {
        println(categoryMessage)

        itemService.loadAll()
            .forEach { item ->
                println(item.showItem())
            }
    }
}

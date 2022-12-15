package com.order.cli.printer

import com.order.cli.interfaces.Printer
import org.springframework.stereotype.Component

@Component
class ProductPrinter : Printer {
    private var categoryMessage = "상품번호                 상품명               판매가격                재고수"
    override fun show() {
        println(categoryMessage)
    }
}

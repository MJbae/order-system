package com.order.cli.printer

import com.order.cli.interfaces.Printer
import org.springframework.stereotype.Component

@Component
class ByePrinter : Printer {
    private var message = "주문해주셔서 감사합니다."
    override fun show() {
        println(this.message)
    }
}

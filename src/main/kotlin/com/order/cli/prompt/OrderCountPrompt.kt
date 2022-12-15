package com.order.cli.prompt

import com.order.cli.interfaces.Prompt
import org.springframework.stereotype.Component

@Component
class OrderCountPrompt : Prompt {
    private val message = "주문수량: "

    override fun display() {
        print(this.message)
    }
}

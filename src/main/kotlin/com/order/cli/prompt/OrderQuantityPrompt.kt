package com.order.cli.prompt

import com.order.cli.interfaces.Prompt
import org.springframework.stereotype.Component

@Component
class OrderQuantityPrompt : Prompt {
    override fun display() {
        print("주문수량: ")
    }
}

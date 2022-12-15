package com.order.cli.prompt

import com.order.cli.interfaces.Prompt
import org.springframework.stereotype.Component

@Component
class ItemIdPrompt : Prompt {
    private val message = "상품번호: "

    override fun display() {
        print(this.message)
    }
}

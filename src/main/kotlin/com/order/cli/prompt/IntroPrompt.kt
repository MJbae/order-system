package com.order.cli.prompt

import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.shell.jline.PromptProvider
import org.springframework.stereotype.Component

@Component
class IntroPrompt : PromptProvider {
    private val message = "입력[o(order): 주문, q(quit): 종료]: "
    override fun getPrompt(): AttributedString {
        return AttributedString(
            this.message,
            AttributedStyle.DEFAULT.background(AttributedStyle.BLACK)
        )
    }
}

package com.order.cli

import com.order.cli.printer.ByePrinter
import com.order.cli.printer.ProductPrinter
import com.order.cli.prompt.ItemIdPrompt
import com.order.cli.prompt.OrderCountPrompt
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.commands.Quit
import kotlin.system.exitProcess

@ShellComponent
class Command(
    private val productPrinter: ProductPrinter,
    private val byePrinter: ByePrinter,
    private val itemIdPrompt: ItemIdPrompt,
    private val orderCountPrompt: OrderCountPrompt
) : Quit.Command {
    @ShellMethod(key = ["order", "o"], value = "order")
    fun order() {
        productPrinter.show()

        while (true) {
            itemIdPrompt.display()
            val itemInput = readlnOrNull()

            if (itemInput.equals(" ")) {
                break
            }

            orderCountPrompt.display()
            val countInput = readlnOrNull()

            println("상품번호: $itemInput, 주문수량: $countInput")
        }
    }

    @ShellMethod(key = ["quit", "q"], value = "quit")
    fun quit() {
        byePrinter.show()
        exitProcess(1)
    }
}
